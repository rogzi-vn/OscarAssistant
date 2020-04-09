package vn.vistark.oscarassistant.core.actions

import android.annotation.SuppressLint
import vn.vistark.oscarassistant.core.reply_libs.ReplyOpeningApplication
import vn.vistark.oscarassistant.ui.main.MainActivity
import vn.vistark.oscarassistant.utils.ApplicationUtils

class OpenApplication {
    companion object {
        val regex = arrayListOf(
            "((mở|khởi động|bật) (ứng dụng |app ){0,1})(.*?)( (coi|cho|giúp|đi|hộ).*)",
            "((mở|khởi động|bật) (ứng dụng |app ){0,1})(.*?)$"
        )

        @SuppressLint("DefaultLocale")
        fun check(context: MainActivity, msg: String): Boolean {
            regex.forEach { rgx ->
                if (rgx.toRegex().containsMatchIn(msg.toLowerCase())) {
                    val rplRgx = rgx
                        .replace("(.*?)$", "")
                        .replace("(.*?)", "|")
                        .replace("$", "")
                    val app = msg.toLowerCase().replace(rplRgx.toRegex(), "").capitalize()
                    var res = false
                    if (ApplicationUtils.openApplication(context, app)) {
                        ReplyOpeningApplication(context, app)
                        res = true
                    }
                    return res
                }
            }
            return false
        }
    }
}