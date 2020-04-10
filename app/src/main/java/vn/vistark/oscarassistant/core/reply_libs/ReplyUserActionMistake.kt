package vn.vistark.oscarassistant.core.reply_libs

import vn.vistark.oscarassistant.ui.main.MainActivity

class ReplyUserActionMistake(val context: MainActivity) {
    init {
        val response =
            arrayListOf(
                "Vâng ạ, vậy tôi xin phép ẩn đi",
                "Vâng ạ, vậy tôi sẽ ẩn đi, nếu có gì hãy gọi tôi nha",
                "Vâng ạ, không sao đâu ạ",
                "Vâng ạ, khi nào cần hãy gọi tôi nhé",
                "Vâng ạ, vậy tôi ẩn xuống đây ạ",
                "Vâng ạ, tôi xin phép ẩn đi ngay",
                "Vâng ạ, vậy hẹn gặp lại bạn ở lần khác"
            )
        context.convertTextToSpeech.talkAndExit(response.random())
    }
}