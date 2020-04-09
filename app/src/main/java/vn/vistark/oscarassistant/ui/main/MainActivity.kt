package vn.vistark.oscarassistant.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import vn.vistark.oscarassistant.R
import vn.vistark.oscarassistant.core.actions.ByeBye
import vn.vistark.oscarassistant.core.reply_libs.ReplyHotwordTrigger
import vn.vistark.oscarassistant.core.prefs.Prefs
import vn.vistark.oscarassistant.core.services.OscarServices
import vn.vistark.oscarassistant.core.services.ServiceCallbacks
import vn.vistark.oscarassistant.core.speech_to_text.ConvertSpeechToText
import vn.vistark.oscarassistant.core.store.Constants
import vn.vistark.oscarassistant.core.text_to_speech.ConvertTextToSpeech
import vn.vistark.oscarassistant.utils.ServicesUtils
import java.util.*


class MainActivity : AppCompatActivity(), ServiceCallbacks {
    val TAG = MainActivity::class.java.simpleName

    companion object {
        var isActive = false
        var AUTO_EXIT_AFTER = 30000L
    }

    lateinit var mainUiController: MainUiController
    lateinit var convertTextToSpeech: ConvertTextToSpeech
    lateinit var convertSpeechToText: ConvertSpeechToText
    lateinit var oscarServiceBinding: OscarServiceBinding
    var timerAutoExit: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        setContentView(R.layout.activity_main)
    }

    private fun initialize() {
        Prefs.initialize(this)
        mainUiController = MainUiController(this)
        SpeechToTextBinding.bind(this)
        convertTextToSpeech = ConvertTextToSpeech(this)
        initTriggers()
        initViewsBinding()
        if (!ServicesUtils.isRunning(this, OscarServices::class.java)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(this, OscarServices::class.java))
            } else {
                startService(Intent(this, OscarServices::class.java))
            }
            finish()
        }
    }

    private fun initViewsBinding() {
        mainUiController.updateOscarResponse("Hãy nói, ${Constants.wakeUpHotKey}")
        oscarIdle.setOnClickListener {
            convertSpeechToText.startListen()
        }
    }

    private fun initTriggers() {
        convertTextToSpeech.startSpeechRecognizer = {
            runOnUiThread {
                convertSpeechToText.startListen()
            }
        }
        convertTextToSpeech.onStart = {
            stopAutoExitTimer()
        }
        convertTextToSpeech.onFinished = {
            if (it) {
                stopAutoExitTimer()
                finish()
            } else {
                startAutoExitTimer()
            }
        }
        ReplyHotwordTrigger(this)
    }

    override fun onStart() {
        super.onStart()
        isActive = true
        initialize()
        oscarServiceBinding = OscarServiceBinding(this)
    }

    override fun onStop() {
        super.onStop()
        stopAutoExitTimer()
        isActive = false
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
        convertTextToSpeech.shutdown()
        oscarServiceBinding.stop()
        convertSpeechToText.destroy()
        super.onDestroy()
    }

    override fun triggerCall() {
        convertSpeechToText.startListen()
    }

    override fun triggerClose() {
        ByeBye(this)
    }


    fun stopAutoExitTimer() {
        if (timerAutoExit != null) {
            timerAutoExit?.cancel()
            timerAutoExit = null
        }
    }

    fun startAutoExitTimer() {
        stopAutoExitTimer()
        timerAutoExit = Timer()
        timerAutoExit?.schedule(object : TimerTask() {
            override fun run() {
                this@MainActivity.finish()
            }

        }, AUTO_EXIT_AFTER)
    }
}
