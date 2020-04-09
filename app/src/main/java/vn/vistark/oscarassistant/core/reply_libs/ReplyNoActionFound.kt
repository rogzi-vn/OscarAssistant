package vn.vistark.oscarassistant.core.reply_libs

import vn.vistark.oscarassistant.ui.main.MainActivity

class ReplyNoActionFound(val context: MainActivity, var msg: String) {
    init {
        val response = arrayListOf(
            "Xin lỗi, tôi không hiểu",
            "Xin lỗi, tôi không hiểu lắm",
            "Xin lỗi, tôi biết phải trả lời như thế nào",
            "Xin lỗi, vấn đề này tôi không biết",
            "Bạn thử nói lại giúp tôi với",
            "Bạn lặp lại chậm hơn được không",
            "Bạn nói chậm và rõ hơn một chút được không",
            "Bạn thử nói lại giúp tôi đi",
            "Dường như hơi ồn, nên tôi nghe không rõ",
            "Câu này tôi cảm thấy xa lạ quá",
            "Tôi không hiểu bạn đang muốn nói đến điều gì?",
            "Xin lỗi, tôi thấy hơi khó hiểu"
        )
        context.convertTextToSpeech.talk(response.random(), false)
    }
}