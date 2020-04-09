package vn.vistark.oscarassistant.core.actions

import android.annotation.SuppressLint
import vn.vistark.oscarassistant.core.reply_libs.ReplyCurrentTime
import vn.vistark.oscarassistant.core.reply_libs.ReplyUserActionMistake
import vn.vistark.oscarassistant.ui.main.MainActivity

class UserActionMistake {
    companion object {
        val regex = arrayListOf(
            "nói (lộn|nhầm)",
            "không có gì( hết| đâu| hết á){0,1}",
            "(tôi|tao|anh|chị|bà|cô|chú|bác|thím|mị) (bị ){0,1}(nhầm|lộn)"
        )

        @SuppressLint("DefaultLocale")
        fun check(context: MainActivity, msg: String): Boolean {
            regex.forEach { rgx ->
                if (rgx.toRegex().containsMatchIn(msg.toLowerCase())) {
                    ReplyUserActionMistake(context)
                    return true
                }
            }
            return false
        }
    }
}