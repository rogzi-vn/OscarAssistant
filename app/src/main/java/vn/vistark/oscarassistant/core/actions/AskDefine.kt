package vn.vistark.oscarassistant.core.actions

import android.annotation.SuppressLint
import vn.vistark.oscarassistant.core.reply_libs.ReplyOpeningApplication
import vn.vistark.oscarassistant.ui.main.MainActivity
import vn.vistark.oscarassistant.utils.ApplicationUtils

class AskDefine {
    companion object {
        val regex = arrayListOf(
            "(cho(.*)hỏi |định nghĩa |khái niệm (về ){0,1}){0,1}(thông tin( về){0,1} ){0,1}(.*?)(.là.+){0,1}$"
        )

        @SuppressLint("DefaultLocale")
        fun check(context: MainActivity, msg: String): Boolean {
            regex.forEach { rgx ->
                if (rgx.toRegex().containsMatchIn(msg.toLowerCase())) {
                    val rplRgx = rgx
                        .replace("(((.*?)))$", "")
                        .replace("(((.*?)))", "|")
                        .replace("$", "")
                    val objectName = msg.toLowerCase().replace(rplRgx.toRegex(), "").capitalize()
                    println("$objectName >>>>>>>>>>>>>>>>>>>>>>>>")
                    context.convertTextToSpeech.talkAndExit("$objectName ư? Để mai nhé")
                    return true
                }
            }
            return false
        }
    }
}