package vn.vistark.oscarassistant.ui.main

import android.os.Build
import java.time.format.DateTimeFormatter

class DaySession {
    companion object {
        val BUOI_SANG = "Buổi Sáng"
        val BUOI_TRUA = "Buổi Trưa"
        val BUOI_CHIEU = "Buổi Chiều"
        val BUOI_TOI = "Buổi Tối"
        val KHUYA = "Khuya"
        val RANG_SANG = "Rạng Sáng"

        fun check(hour: Int, minutes: Int): String {
            val inp = String.format("%02d:%02d", hour, minutes)
            return if ("00:00" < inp && inp < "06:30") {
                RANG_SANG
            } else if (inp <= "10:30") {
                BUOI_SANG
            } else if (inp <= "14:00") {
                BUOI_TRUA
            } else if (inp <= "17:30") {
                BUOI_CHIEU
            } else if (inp <= "21:30") {
                BUOI_TOI
            } else {
                KHUYA
            }
        }
    }
}