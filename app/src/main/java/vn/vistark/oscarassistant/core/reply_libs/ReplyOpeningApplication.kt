package vn.vistark.oscarassistant.core.reply_libs

import vn.vistark.oscarassistant.ui.main.MainActivity

class ReplyOpeningApplication(val context: MainActivity, val appName: String) {
    init {
        val response =
            arrayListOf(
                "Đang mở ứng dụng cho bạn",
                "$appName của bạn đây",
                "Hoàn tất mở $appName",
                "Ứng dụng $appName của bạn đây",
                "Đang khởi động ứng dụng cho bạn",
                "Đã khởi động ứng dụng $appName cho bạn rồi đó",
                "Ứng dụng $appName đã được khởi động"
            )
        context.convertTextToSpeech.talkAndExit(response.random())
    }
}