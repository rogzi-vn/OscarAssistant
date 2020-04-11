package vn.vistark.oscarassistant.ui.main

import android.util.Log
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import vn.vistark.oscarassistant.core.speech_to_text.ConvertSpeechToText

class SpeechToTextBinding {
    companion object {
        fun bind(context: MainActivity) {
            context.convertSpeechToText = ConvertSpeechToText(context)
            context.convertSpeechToText.onStart = {
                context.oscarServiceBinding.stopHotwordDetector()
                context.stopAutoExitTimer()
                context.mainUiController.updateFrame(Fragment())
                context.convertTextToSpeech.stop()
                context.userMessage.text = ""
                context.mainUiController.hideOscarIdle()
            }
            context.convertSpeechToText.onPartialResult = { currentMsg ->
                if (currentMsg.isNotEmpty()) {
                    context.userMessage.text = currentMsg
                }
            }
            context.convertSpeechToText.onResult = {
                var resultMsg = ""
                if (it == null) {
                    resultMsg = "Gì cơ, tôi nghe không rõ lắm"
                } else {
                    resultMsg = it
                    context.userMessage.text = it
                    ProcessingTask(context, resultMsg).execute()
                }
            }
            context.convertSpeechToText.onFinished = {
                context.oscarServiceBinding.startHotwordDetector()
                context.startAutoExitTimer()
                context.runOnUiThread {
                    context.mainUiController.hideBottomSheet()
                    context.oscarServiceBinding.startHotwordDetector()
                }
            }
        }
    }
}