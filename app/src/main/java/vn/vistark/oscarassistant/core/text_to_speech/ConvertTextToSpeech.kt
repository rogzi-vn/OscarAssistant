package vn.vistark.oscarassistant.core.text_to_speech

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import com.google.gson.GsonBuilder
import org.json.JSONObject
import vn.vistark.oscarassistant.core.services.OscarServices
import java.lang.Exception
import java.util.*


class ConvertTextToSpeech(private val context: Context) {
    val TAG = ConvertTextToSpeech::class.java.simpleName

    private var audioManager: AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var previousStreamVolume = -1
    // Default TTS
    private var tts: TextToSpeech? = null

    var onStart: (() -> Unit)? = null
    var onFinished: ((Boolean) -> Unit)? = null
    var exitApp: (() -> Unit)? = null
    var startSpeechRecognizer: (() -> Unit)? = null
    var isStartSpeechRecognizer = false
    var isInitTtsSuccessfull = false
    var isExitApplicationAfterTalk = false
    var isPreventTimerAutoExit = false

    var currentTalkingContent = ""

    init {
        previousStreamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    fun initialize(msg: String) {
        try {
            tts = TextToSpeech(context, TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS) {
                    // Lệnh khi thành công
                    tts!!.language = Locale("vi")
                    tts!!.setPitch(1.23F)
                    tts!!.setSpeechRate(2F)
                    tts!!.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onDone(utteranceId: String?) {
//                            makeDefaultVolume()
                            currentTalkingContent = msg
                            onFinished?.invoke(isPreventTimerAutoExit)
                            if (isExitApplicationAfterTalk) {
                                exitApp?.invoke()
                            }
                            if (isStartSpeechRecognizer) {
                                startSpeechRecognizer?.invoke()
                                isStartSpeechRecognizer = false
                            }
                        }

                        override fun onError(utteranceId: String?) {
//                            makeDefaultVolume()
                            currentTalkingContent = msg
                            if (isExitApplicationAfterTalk) {
                                exitApp?.invoke()
                            }
                            onFinished?.invoke(isPreventTimerAutoExit)
                        }

                        override fun onStart(utteranceId: String?) {
//                            makeMaxVolume()
                            onStart?.invoke()
                        }
                    })
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //vi-vn-x-vif-local (Giọng nam Bắc - nói offline)
                        //vi-vn-x-vid-network (Giọng nam Bắc - off)
                        //vi-vn-x-vic-network (Giọng nữ Bắc - off)
                        //vi-vn-x-vif-network (Giọng nam Nam - off)
                        //vi-vn-x-vie-local (Giọng nữ Nam - off)
                        //vi-vn-x-gft-local (Giọng nữ mặc định- offline - dớ tệ hại)
                        //vi-vn-x-vic-local (Giọng nữ Bắc - offline - ngữ điệu - HAY)
                        //vi-vn-x-vid-local (Giọng nam Nam - offline - khá dở)
                        //vi-vn-x-vie-network (Giọng nữ Nam - offline - khá dở)
                        //vi-vn-x-gft-network (Giọng nữ Nam - offline - dở)
                        //vi-VN-language (Mặc định)

                        if (OscarServices.oscarCurrentVoice == null) {
                            val name = "vi-vn-x-vic-local"
                            if (!tts!!.defaultVoice.name.contains(name)) {
                                for (v in tts!!.voices) {
                                    if (v.name.contains(name)) {
                                        tts!!.voice = v
                                        OscarServices.oscarCurrentVoice = v
                                        break
                                    }
                                }
                            }
                        } else {
                            tts!!.voice = OscarServices.oscarCurrentVoice
                        }
                    }

                    isInitTtsSuccessfull = true

                    talk(msg, isStartSpeechRecognizer)
                } else {
                    isInitTtsSuccessfull = false
                }
            })
        } catch (e: Exception) {
            isInitTtsSuccessfull = false
            e.printStackTrace()
        }
    }

    fun talk(msg: String, isStartSpeechRecognizer: Boolean = false) {
        this.isStartSpeechRecognizer = isStartSpeechRecognizer
        this.isExitApplicationAfterTalk = false
        this.isPreventTimerAutoExit = false
        if (isInitTtsSuccessfull) {
            if (msg.isNotEmpty() && msg == currentTalkingContent)
                return
            stop()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts?.speak(
                    msg,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED
                )
            } else {
                tts?.speak(msg, TextToSpeech.QUEUE_FLUSH, null)
            }
        } else {
            initialize(msg)
        }
    }

    fun talkAndPreventTimer(msg: String) {
        talk(msg, false)
        this.isPreventTimerAutoExit = true
    }

    fun talkAndExit(msg: String) {
        talk(msg, false)
        this.isExitApplicationAfterTalk = true
    }

    fun makeMaxVolume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            previousStreamVolume =
                audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            if (!audioManager.isStreamMute(AudioManager.STREAM_MUSIC)) {
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                    AudioManager.FLAG_PLAY_SOUND
                )
            }
        }
    }

    fun makeDefaultVolume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!audioManager.isStreamMute(AudioManager.STREAM_MUSIC)) {
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    previousStreamVolume,
                    AudioManager.FLAG_PLAY_SOUND
                )
            }
        }
    }

    val isTalking: Boolean
        get() {
            return tts != null && tts!!.isSpeaking
        }

    fun stop() {
        if (isTalking) {
            tts!!.stop()
        }
    }

    fun shutdown() {
        stop()
        tts?.shutdown()
        tts = null
    }
}