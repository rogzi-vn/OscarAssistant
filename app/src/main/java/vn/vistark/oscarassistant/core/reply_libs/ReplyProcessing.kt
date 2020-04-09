package vn.vistark.oscarassistant.core.reply_libs

import vn.vistark.oscarassistant.ui.main.MainActivity

class ReplyProcessing(val context: MainActivity) {
    init {
        val response =
            arrayListOf("Đang xử lý, vui lòng đợi", "Tôi sẽ hoàn tất nhanh thôi", "Bạn đợi xíu nhé")
        context.convertTextToSpeech.talk(response.random(), false)
    }
}