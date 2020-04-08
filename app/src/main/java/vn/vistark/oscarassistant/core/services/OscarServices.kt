package vn.vistark.oscarassistant.core.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import vn.vistark.oscarassistant.core.hotword_detector.HotWordDetector
import vn.vistark.oscarassistant.core.store.Constants
import vn.vistark.oscarassistant.ui.main.MainActivity


class OscarServices : Service() {
    val TAG = OscarServices::class.java.simpleName
    val binder = LocalBinder()
    var serviceCallbacks: ServiceCallbacks? = null


    lateinit var hotwordDetector: HotWordDetector

    fun initHotwordDetector() {
        hotwordDetector = HotWordDetector(this, Constants.hotKey)
        hotwordDetector.execute()
        hotwordDetector.onDetected = {
            if (!MainActivity.isActive) {
//                Log.w(TAG, "Khởi động màn hình chính")
                val i = Intent(this, MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                startActivity(i)
            } else {
//                Log.w(TAG, "Gọi sự kiện")
                serviceCallbacks?.trigger()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        initHotwordDetector()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    inner class LocalBinder : Binder() {
        val services: OscarServices
            get() = this@OscarServices
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    fun setCallback(callbacks: ServiceCallbacks?) {
        serviceCallbacks = callbacks
    }
}
