package vn.vistark.oscarassistant.ui.main

import android.content.Context
import kotlinx.android.synthetic.main.activity_main.*
import vn.vistark.oscarassistant.core.speech_to_text.ConvertSpeechToText
import java.util.*

class SpeechToTextBinding {
    companion object {
        fun bind(context: MainActivity) {
            context.convertSpeechToText = ConvertSpeechToText(context)
            context.convertSpeechToText.onStart = {
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
                    // Xử lý
                }
                context.convertTextToSpeech.talk(resultMsg, false)
            }
            context.convertSpeechToText.onFinished = {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        context.runOnUiThread {
                            context.mainUiController.hideBottomSheet()
                            context.oscarServiceBinding.startHotwordDetector()
                        }
                    }
                }, 500)
            }
        }
    }
}