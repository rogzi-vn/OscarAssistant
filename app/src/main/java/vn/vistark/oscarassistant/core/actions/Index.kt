package vn.vistark.oscarassistant.core.actions

import vn.vistark.oscarassistant.ui.main.MainActivity

class Index {
    companion object {
        fun processing(context: MainActivity, msg: String): Boolean {
            return when {
                OpenApplication.check(context, msg) -> true
                AskCurrentTime.check(context, msg) -> true
                AskDefine.check(context, msg) -> true
                else -> UserActionMistake.check(context, msg)
            }
        }
    }
}
