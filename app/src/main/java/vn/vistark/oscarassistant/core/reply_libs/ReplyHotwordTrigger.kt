package vn.vistark.oscarassistant.core.reply_libs

import vn.vistark.oscarassistant.ui.main.MainActivity

class ReplyHotwordTrigger(val context: MainActivity) {
    init {
        val res = arrayListOf(
            "Vâng, bạn cần gì?",
            "Vâng, tôi có thể giúp gì cho bạn?",
            "Vâng, tôi đây!",
            "Vâng, hãy cho tôi biết bạn cần tôi giúp gì",
            "Vâng, có tôi",
            "Vâng, tôi đây ạ",
            "Vâng, tôi có thể giúp được gì ạ",
            "Vâng ạ",
            "Vâng, tôi xin nghe",
            "Vâng, tôi nghe ạ"
        )
        context.convertTextToSpeech.talk(res.random(), true)
    }
}