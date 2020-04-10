package vn.vistark.oscarassistant.core.actions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import vn.vistark.oscarassistant.core.models.wikipedia.NumberResponse
import vn.vistark.oscarassistant.ui.define_info.DefineInfoFragment
import vn.vistark.oscarassistant.ui.main.MainActivity

class AskDefine {
    companion object {
        val TAG = AskDefine::class.java.simpleName

        val regex = arrayListOf(
            "(cho(.*)hỏi |định nghĩa |khái niệm (về ){0,1}){0,1}(thông tin( về){0,1} ){0,1}(.*?)(.là.+){0,1}$"
        )

        @SuppressLint("DefaultLocale")
        fun check(context: MainActivity, msg: String): Boolean {
            regex.forEach { rgx ->
                if (rgx.toRegex().containsMatchIn(msg.toLowerCase())) {
                    val rplRgx = rgx
                        .replace("(((.*?)))$", "")
                        .replace("(((.*?)))", "|")
                        .replace("$", "")
                    val objectName = msg.toLowerCase().replace(rplRgx.toRegex(), "").capitalize()
                    println("$objectName >>>>>>>>>>>>>>>>>>>>>>>>")
                    return getDefine(context, objectName)
                }
            }
            return false
        }

        fun getDefine(context: MainActivity, key: String): Boolean {
            try {
                val wdResponse = APIUtils.mAPIServices?.getWikiDefineDescriptionInfo(key)?.execute()
                if (wdResponse != null && wdResponse.isSuccessful && wdResponse.body() != null) {
                    val wdr = wdResponse.body()!!
//                    Log.w(TAG, wdr.query.pages.toString())
//                    Log.w(TAG, wdr.query.pages.entrySet().first().value.toString())
                    val nr = NumberResponse.extract(wdr.query.pages.entrySet().first().value)
                    if (nr.extract != null && nr.extract!!.isNotEmpty()) {
                        val summaryStr = nr.extract!!.replace("\\((.*?)\\)".toRegex(), "")

                        val objectName = nr.title
                        val dif = DefineInfoFragment()
                        val bd = Bundle()
                        bd.putString(DefineInfoFragment.FULL_CONTENT, nr.extract!!)
                        bd.putString(DefineInfoFragment.OBJECT_NAME, objectName)
                        dif.arguments = bd
                        context.mainUiController.updateFrame(dif)
                        val s = "^((.*?)\\.){1,2}".toRegex().find(
                            summaryStr
                        )!!.value
                        Log.w(TAG, s)
                        context.convertTextToSpeech.talkAndPreventTimer(s)
                        return true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
    }
}