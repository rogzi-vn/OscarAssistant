package vn.vistark.oscarassistant.core.reply_libs

import vn.vistark.oscarassistant.ui.main.DaySession
import vn.vistark.oscarassistant.ui.main.MainActivity
import java.util.*

class ReplyCurrentTime(context: MainActivity) {
    init {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        val res = arrayListOf(
            "Bây giờ là $hour giờ $minutes phút ạ",
            "Hiện tại là $hour giờ $minutes phút ạ",
            "Lúc này là $hour giờ $minutes phút ạ",
            "Bây giờ đang là $hour giờ $minutes phút ạ",
            "Hiện tại đang là $hour giờ $minutes phút ạ",
            "Lúc này đang là $hour giờ $minutes phút ạ",
            "Đang là $hour giờ $minutes phút thưa bạn"
        )
        val timeStr = String.format("%02d:%02d", hour, minutes)
        if ("05:30" <= timeStr && timeStr <= "07:00") {
            res.addAll(
                arrayListOf(
                    "Bây giờ là $hour giờ $minutes phút sáng. Rất thích hợp nếu bạn tập một bài thể dục nhẹ đó",
                    "Hiện tại là $hour giờ $minutes phút buổi sáng. Bạn đã ăn sáng chưa",
                    "Lúc này là $hour giờ $minutes phút sáng. Bạn nên ăn gì đó để có sức cho cả ngày dài nhé",
                    "Bây giờ đang là $hour giờ $minutes phút buổi sáng. Chúc bạn một buổi sáng đầy năng động",
                    "Hiện tại đang là $hour giờ $minutes phút sáng. Chúc bạn một ngày tốt lành",
                    "Lúc này đang là $hour giờ $minutes phút sáng. Chúc bạn có một ngày tuyệt vời nhé"
                )
            )
        }
        if ("11:00" <= timeStr && timeStr <= "11:30") {
            res.addAll(
                arrayListOf(
                    "Bây giờ là $hour giờ $minutes phút trưa. Bạn nên chuẩn bị gì đó cho bữa trưa đi",
                    "Hiện tại là $hour giờ $minutes phút buổi trưa. Bạn đã ăn trưa chưa",
                    "Lúc này là $hour giờ $minutes phút trưa. Về nhà ăn trưa thôi nào",
                    "Bây giờ đang là $hour giờ $minutes phút trưa. Hãy cân nhắc ăn trưa rồi nghỉ ngơi đi bạn nhé",
                    "Hiện tại đang là $hour giờ $minutes phút buổi trưa. Cân nhắc ăn và nghỉ trưa đi nha",
                    "Lúc này đang là $hour giờ $minutes phút trưa. Nghỉ ngơi thôi nào"
                )
            )
        }
        if ("17:00" <= timeStr && timeStr <= "18:00") {
            res.addAll(
                arrayListOf(
                    "Bây giờ là $hour giờ $minutes phút. Hãy về nhà và chuẩn bị cho bữa tối thôi nào",
                    "Hiện tại là $hour giờ $minutes phút. Chuẩn bị về nhà ăn tối thôi",
                    "Lúc này là $hour giờ $minutes phút. Sắp hết ngày rồi đó"
                )
            )
        }
        if ("21:00" <= timeStr && timeStr <= "23:30") {
            res.addAll(
                arrayListOf(
                    "Bây giờ là $hour giờ $minutes phút tối. Vệ sinh cá nhân và chuẩn bị đi ngủ thôi nào",
                    "Hiện tại là $hour giờ $minutes phút tối. Thư giãn và chuẩn bị ngủ nghỉ thôi bạn ơi",
                    "Lúc này là $hour giờ $minutes phút tối. Bạn đã đánh răng rửa mặt chưa nào",
                    "Bây giờ đang là $hour giờ $minutes phút tối rồi đó, ngủ sớm đi bạn ơi",
                    "Hiện tại đang là $hour giờ $minutes phút rồi, ngủ giờ này tốt cho sức khỏe của bạn đó",
                    "Lúc này đang là $hour giờ $minutes phút rồi, ngủ thôi, đừng thức khyua nhé"
                )
            )
        }
        if ("23:30" < timeStr && timeStr <= "23:59") {
            res.addAll(
                arrayListOf(
                    "Bây giờ là $hour giờ $minutes phút, quá khuya để thức rồi đó",
                    "Hiện tại là $hour giờ $minutes phút, bạn không nên thức quá khuya như vậy",
                    "Lúc này là $hour giờ $minutes phút, thức như vậy không tốt cho sức khỏe của bạn đâu nha",
                    "Bây giờ đang là $hour giờ $minutes phút, ngủ đi nào, khuya lắm rồi đó",
                    "Hiện tại đang là $hour giờ $minutes phút, hãy gạt bỏ suy tư mà ngủ thôi nào",
                    "Lúc này đang là $hour giờ $minutes phút, đừng lo lắng gì nữa, hãy ngủ đi nào"
                )
            )
        }
        if ("00:00" < timeStr && timeStr <= "03:00") {
            res.addAll(
                arrayListOf(
                    "Bây giờ là $hour giờ $minutes phút, sao bạn lại thức đến giờ này cơ chứ?",
                    "Hiện tại là $hour giờ $minutes phút, nếu thức đến giờ này, bạn nên ngủ một chút đi",
                    "Lúc này là $hour giờ $minutes phút, bạn không bên thức đến mờ sáng như thế này đâu",
                    "Bây giờ đang là $hour giờ $minutes phút, nếu chưa ngủ, hãy chợp mắt một chút đi bạn nhé"
                )
            )
        }
        context.convertTextToSpeech.talk(res.random(), false)
    }
}