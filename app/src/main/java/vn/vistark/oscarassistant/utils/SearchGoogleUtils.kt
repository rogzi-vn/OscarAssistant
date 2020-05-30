package vn.vistark.oscarassistant.utils

import android.content.Context
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class SearchGoogleUtils {
    companion object {
        fun search(key: String) {
            val url = URL("https://www.google.com/search?q=$key")
            val res = url.readText(Charset.forName("UTF-8"))
                .replace("  ", " ")
                .replace("  ", " ")
                .replace("\r\n", "")
                .replace("\t", "")

            println("$res >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

            "/url\\?q=(.*?)\">".toRegex().findAll(res).forEach { f ->
                val m = f.value
                val idx = f.range
                println("$m found at indexes: $idx")
            }
        }
    }
}