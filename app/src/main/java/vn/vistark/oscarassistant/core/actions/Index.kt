package vn.vistark.oscarassistant.core.actions

import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import vn.vistark.oscarassistant.ui.main.MainActivity
import vn.vistark.oscarassistant.utils.SearchGoogleUtils

class Index {
    companion object {
        fun processing(context: MainActivity, msg: String): Boolean {
//            SearchGoogleUtils.search("sơn tùng")
            return when {
                OpenApplication.check(context, msg) -> true
                AskCurrentTime.check(context, msg) -> true
                AskDefine.check(context, msg) -> true
                else -> UserActionMistake.check(context, msg)
            }
        }
    }
}
