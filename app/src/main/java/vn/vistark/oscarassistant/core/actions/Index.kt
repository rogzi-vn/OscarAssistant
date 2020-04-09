package vn.vistark.oscarassistant.core.actions

import vn.vistark.oscarassistant.ui.main.MainActivity

class Index {
    companion object {
        fun processing(context: MainActivity, msg: String): Boolean {
            return when {
                UserActionMistake.check(context, msg) -> true
                AskCurrentTime.check(context, msg) -> true
                AskDefine.check(context, msg) -> true
                else -> OpenApplication.check(context, msg)
            }
        }
    }
}