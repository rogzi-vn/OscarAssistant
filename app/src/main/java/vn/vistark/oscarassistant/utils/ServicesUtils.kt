package vn.vistark.oscarassistant.utils

import android.app.ActivityManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService


class ServicesUtils {
    companion object {
        fun isRunning(context: Context, serviceClass: Class<*>): Boolean {
            val manager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
            return false
        }
    }
}