package vn.vistark.oscarassistant.core.actions

import vn.vistark.oscarassistant.core.Constants
import vn.vistark.oscarassistant.ui.main.MainActivity

class ByeBye(val context: MainActivity) {
    init {
        val response =
            arrayListOf(
                "Vâng, Hẹn gặp lại",
                "Vâng, Hãy gọi tôi nếu cần nhé",
                "Vâng, Hãy nói ${Constants.wakeUpHotKey} khi cần nhé",
                "Vâng, tôi nghỉ đây",
                "Vâng, Bye bạn",
                "Vâng ạ, vậy tôi xin phép ẩn đi",
                "Vâng ạ, tôi sẽ ẩn xuống ngay"
            )
        context.convertTextToSpeech.talkAndExit(response.random())
    }
}