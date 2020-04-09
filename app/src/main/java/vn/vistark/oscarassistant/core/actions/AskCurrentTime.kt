package vn.vistark.oscarassistant.core.actions

import android.annotation.SuppressLint
import vn.vistark.oscarassistant.core.reply_libs.ReplyCurrentTime
import vn.vistark.oscarassistant.ui.main.MainActivity

class AskCurrentTime {
    companion object {
        val regex = arrayListOf(
            "(bây giờ|lúc này|hiện tại|đang)(.*?)(là ){0,1}mấy giờ",
            "(.*?)(là ){0,1}mấy giờ rồi"
        )

        @SuppressLint("DefaultLocale")
        fun check(context: MainActivity, msg: String): Boolean {
            regex.forEach { rgx ->
                if (rgx.toRegex().containsMatchIn(msg.toLowerCase())) {
                    ReplyCurrentTime(context)
                    return true
                }
            }
            return false
        }
    }
}