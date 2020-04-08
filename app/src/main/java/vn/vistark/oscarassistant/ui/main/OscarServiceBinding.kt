package vn.vistark.oscarassistant.ui.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import vn.vistark.oscarassistant.core.services.OscarServices

class OscarServiceBinding(val context: MainActivity) : ServiceConnection {
    var oscarServices: OscarServices? = null
    var bound = false

    init {
        val intent = Intent(context, OscarServices::class.java)
        context.bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    fun startHotwordDetector() {
        oscarServices?.hotwordDetector?.waitHotword()
    }

    fun stop() {
        startHotwordDetector()
        if (bound) {
            oscarServices?.setCallback(null)
            context.unbindService(this)
            bound = false
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        bound = false
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        if (service != null) {
            val binder: OscarServices.LocalBinder = service as OscarServices.LocalBinder
            oscarServices = binder.services
            bound = true
            oscarServices?.setCallback(context)
        } else {
            bound = false
        }
    }

}