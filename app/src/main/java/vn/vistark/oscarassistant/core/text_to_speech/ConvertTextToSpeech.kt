package vn.vistark.oscarassistant.core.text_to_speech

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import org.json.JSONObject
import java.lang.Exception
import java.util.*


class ConvertTextToSpeech(private val context: Context) {
    val TAG = ConvertTextToSpeech::class.java.simpleName

    lateinit var audioManager: AudioManager
    private var previousStreamVolume = -1
    // Default TTS
    private var tts: TextToSpeech? = null

    var onFinished: (() -> Unit)? = null

    init {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        previousStreamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    fun talk(msg: String, isStartSpeechRecognizer: Boolean = true) {
        stop()
        tts = TextToSpeech(context, TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS) {
                // Lệnh khi thành công
                tts!!.language = Locale("vi")
                tts!!.setPitch(1.23F)
                tts!!.setSpeechRate(2F)
                tts!!.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onDone(utteranceId: String?) {
                        //makeDefaultVolume()
                        tts!!.shutdown()
                        if (isStartSpeechRecognizer) {
                            onFinished?.invoke()
                        }
                    }

                    override fun onError(utteranceId: String?) {
                        //makeDefaultVolume()
                    }

                    override fun onStart(utteranceId: String?) {
                        //makeMaxVolume()
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

                    val name = "vi-vn-x-vic-local"
                    try {
                        if (!tts!!.defaultVoice.name.contains(name)) {
                            for (v in tts!!.voices) {
                                if (v.name.contains(name)) {
                                    tts!!.voice = v
                                    break
                                }
                            }
                        }
                    } catch (e: Exception) {
                    }
                    tts!!.speak(
                        msg,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED
                    )
                } else {
                    tts!!.speak(msg, TextToSpeech.QUEUE_FLUSH, null)
                }
            }
        })
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

    private fun isTalking(): Boolean {
        return tts?.isSpeaking ?: false
    }

    fun stop() {
        if (tts != null && tts!!.isSpeaking) {
            tts!!.stop()
        }
    }
}