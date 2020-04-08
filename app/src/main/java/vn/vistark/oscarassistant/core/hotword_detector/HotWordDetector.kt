package vn.vistark.oscarassistant.core.hotword_detector

import android.Manifest
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.core.content.ContextCompat
import edu.cmu.pocketsphinx.*
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

class HotWordDetector(private val context: Context, var hotword: String) :
    AsyncTask<Void?, Void?, Exception?>(), RecognitionListener {
    private val TAG = HotWordDetector::class.java.simpleName

    private val KWS_SEARCH = "wakeup"

    var onFinised: ((Boolean) -> Unit)? = null
    var onDetected: (() -> Unit)? = null

    private var recognizer: SpeechRecognizer? = null
    var activityReference: WeakReference<Context> = WeakReference(context)

    override fun onPreExecute() {
        super.onPreExecute()

        hotword = hotword.toLowerCase(Locale.getDefault())

        val permissionGrantedCheck =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) + ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) + ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        if (permissionGrantedCheck != 0) {
            throw java.lang.Exception("Miss RECORD_AUDIO, READ_EXTERNAL_STORAGE or WRITE_EXTERNAL_STORAGE permission")
        } else {
            Log.w(TAG, "Full necessary permission granted")
        }
    }

    override fun doInBackground(vararg params: Void?): Exception? {
        try {
            val assets = Assets(activityReference.get())
            val assetDir: File = assets.syncAssets()
            setupRecognizer(assetDir)
        } catch (e: IOException) {
            return e
        }
        return null
    }

    override fun onPostExecute(result: Exception?) {
        super.onPostExecute(result)
        if (result != null) {
            onFinised?.invoke(false)
            result.printStackTrace()
        } else {
            waitHotword()
        }
    }

    private fun setupRecognizer(assetsDir: File) {
        recognizer = SpeechRecognizerSetup.defaultSetup()
            .setAcousticModel(File(assetsDir, "en-us-ptm"))
            .setDictionary(File(assetsDir, "cmudict-en-us.dict"))
//            .setRawLogDir(assetsDir)
            .recognizer
        recognizer!!.addListener(this)

        // Create keyword-activation search.
        recognizer!!.addKeyphraseSearch(
            KWS_SEARCH,
            hotword
        )
    }

    fun waitHotword() {
        stopListen()
        recognizer?.startListening(KWS_SEARCH)
    }

    fun stopListen() {
        recognizer?.stop()
    }

    override fun onResult(hypothesis: Hypothesis?) {
        if (hypothesis != null) {
            val text: String = hypothesis.hypstr
            if (text == hotword) {
                onDetected?.invoke()
                stopListen()
            }
        }
    }

    override fun onPartialResult(hypothesis: Hypothesis?) {
        if (hypothesis == null) return

        val text: String = hypothesis.hypstr

        if (text == hotword) {
            onDetected?.invoke()
            stopListen()
        }
    }

    override fun onTimeout() {
        waitHotword()
    }

    override fun onBeginningOfSpeech() {
    }

    override fun onEndOfSpeech() {
        waitHotword()
    }

    override fun onError(p0: java.lang.Exception?) {
        p0?.printStackTrace()
    }
}