package vn.vistark.oscarassistant.ui.main

import android.os.AsyncTask
import vn.vistark.oscarassistant.core.actions.Index
import vn.vistark.oscarassistant.core.reply_libs.ReplyNoActionFound
import vn.vistark.oscarassistant.core.reply_libs.ReplyProcessing

class ProcessingTask(val context: MainActivity, var resultMsg: String) :
    AsyncTask<Void, Void, Unit>() {
    override fun onPreExecute() {
        super.onPreExecute()
        ReplyProcessing(context)
    }

    override fun doInBackground(vararg params: Void?) {
        if (!Index.processing(context, resultMsg)) {
            ReplyNoActionFound(
                context,
                resultMsg
            )
        }
    }
}