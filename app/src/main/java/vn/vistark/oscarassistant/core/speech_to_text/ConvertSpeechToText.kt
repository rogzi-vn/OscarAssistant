package vn.vistark.oscarassistant.core.speech_to_text

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log

class ConvertSpeechToText(private val context: Context) : RecognitionListener {
    val TAG = ConvertSpeechToText::class.java.simpleName

    var mSpeechRecognizerManager: SpeechRecognizer? = null
    private var mSpeechRecognizerIntent: Intent? = null
    private var language = "vi-VN"
    private var timeout = 10000L
    private var mIsListening = false
    private var mIsDoneSetupSpeechRecognizer = false

    var onStart: (() -> Unit)? = null
    var onResult: ((String?) -> Unit)? = null
    var onPartialResult: ((String) -> Unit)? = null
    var onFinished: (() -> Unit)? = null

    init {
        initSpeechRecognizerManger()
    }

    // Hàm dùng để khởi tạo bộ điều khiển nhận diện giọng nói
    private fun initSpeechRecognizerManger() {
        // Khởi tạo SRM
        mSpeechRecognizerManager = SpeechRecognizer.createSpeechRecognizer(context)
        if (mSpeechRecognizerManager != null) {
            mSpeechRecognizerManager!!.setRecognitionListener(this)

            // Khởi tạo intent
            mSpeechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            if (mSpeechRecognizerIntent != null) {
                mSpeechRecognizerIntent!!.run {
                    putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, language)
                    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                    putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language)
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true) // For streaming result
                    putExtra(
                        RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
                        timeout
                    )
                }
                mIsDoneSetupSpeechRecognizer = true
            }
        }
    }

    //region Khu vực dành cho phần nhận diện giọng nói
    fun startListen() {
        if (!isListening()) {
            Log.w(TAG, ">>>> NGHE")
            if (!mIsDoneSetupSpeechRecognizer) {
                initSpeechRecognizerManger()
            }
            onStart?.invoke()
            mSpeechRecognizerManager?.startListening(mSpeechRecognizerIntent)
        } else {
            Log.w(TAG, ">>>> ĐÃ NGHE TRƯỚC ĐÓ")
        }
    }

    fun stopListen() {
        if (mSpeechRecognizerManager != null) {
            mSpeechRecognizerManager!!.stopListening()
            mSpeechRecognizerManager!!.cancel()
            mIsListening = false
        }
    }

    fun destroy() {
        stopListen()
        if (mSpeechRecognizerManager != null) {
            mSpeechRecognizerManager!!.destroy()
        }
    }

    fun isListening(): Boolean {
        return mIsListening
    }

    override fun onReadyForSpeech(params: Bundle?) {
    }

    override fun onRmsChanged(rmsdB: Float) {
    }

    override fun onBufferReceived(buffer: ByteArray?) {
    }

    override fun onPartialResults(partialResults: Bundle?) {
        if (partialResults != null) {
            val texts =
                partialResults.getStringArrayList("android.speech.extra.UNSTABLE_TEXT")!!
            onPartialResult?.invoke(texts[0])
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
    }

    override fun onBeginningOfSpeech() {
        mIsListening = true
    }

    override fun onEndOfSpeech() {
        mIsListening = false
        onFinished?.invoke()
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_RECOGNIZER_BUSY) {
            Log.e(TAG, "Trình ghi âm đang bận")
            return
        } else if (error == SpeechRecognizer.ERROR_NO_MATCH) {
            Log.e(TAG, "Không chuyển được")
        } else if (error == SpeechRecognizer.ERROR_NETWORK) {
            Log.e(TAG, "Lỗi mạng")
        } else {
            Log.e(TAG, "Vui lòng cấp quyền ghi âm để tôi có thể nghe bạn nói $error")
        }
        mIsListening = false
        onFinished?.invoke()
    }

    override fun onResults(results: Bundle?) {
        if (results != null) {
            val resultArray =
                results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)!!
            onResult?.invoke(resultArray[0])
        }
    }
}