package vn.vistark.oscarassistant.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import vn.vistark.oscarassistant.R
import vn.vistark.oscarassistant.core.services.OscarServices
import vn.vistark.oscarassistant.core.services.ServiceCallbacks
import vn.vistark.oscarassistant.core.speech_to_text.ConvertSpeechToText
import vn.vistark.oscarassistant.core.text_to_speech.ConvertTextToSpeech
import vn.vistark.oscarassistant.utils.ServicesUtils
import java.util.*


class MainActivity : AppCompatActivity(), ServiceCallbacks {
    val TAG = MainActivity::class.java.simpleName

    companion object {
        var isActive = false
    }

    lateinit var mainUiController: MainUiController
    lateinit var convertTextToSpeech: ConvertTextToSpeech
    lateinit var convertSpeechToText: ConvertSpeechToText
    lateinit var oscarServiceBinding: OscarServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()
    }

    private fun initialize() {
        mainUiController = MainUiController(this)
        SpeechToTextBinding.bind(this)
        convertTextToSpeech = ConvertTextToSpeech(this)
        initTriggers()
        initViewsBinding()
        if (!ServicesUtils.isRunning(this, OscarServices::class.java)) {
            startService(Intent(this, OscarServices::class.java))
            finish()
        }
    }

    private fun initViewsBinding() {
        oscarIdle.setOnClickListener {
            convertSpeechToText.startListen()
        }
    }

    private fun initTriggers() {
        convertTextToSpeech.onFinished = {
            runOnUiThread {
                convertSpeechToText.startListen()
            }
        }
        convertTextToSpeech.talk("Xin chào")
    }

    override fun onStart() {
        super.onStart()
        isActive = true
        oscarServiceBinding = OscarServiceBinding(this)
    }

    override fun onStop() {
        super.onStop()
        isActive = false
        convertTextToSpeech.stop()
        oscarServiceBinding.stop()
    }

    override fun onPause() {
        super.onPause()
        isActive = false
    }

    override fun onResume() {
        super.onResume()
        isActive = true
    }

    override fun onDestroy() {
        super.onDestroy()
        convertSpeechToText.destroy()
    }

    override fun trigger() {
        Log.w(TAG, "Đã nhận được yêu cầu lắng nghe")
        convertSpeechToText.startListen()
    }
}
