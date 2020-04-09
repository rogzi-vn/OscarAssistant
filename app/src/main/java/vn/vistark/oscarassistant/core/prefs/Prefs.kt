package vn.vistark.oscarassistant.core.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class Prefs {
    companion object {
        var sharedPreferences: SharedPreferences? = null
        fun initialize(app: AppCompatActivity) {
            sharedPreferences = app.getSharedPreferences("OscarAssistant", Context.MODE_PRIVATE)
        }
    }
}