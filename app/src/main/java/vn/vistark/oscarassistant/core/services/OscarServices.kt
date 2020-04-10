package vn.vistark.oscarassistant.core.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.speech.tts.Voice
import androidx.core.app.NotificationCompat
import vn.vistark.oscarassistant.R
import vn.vistark.oscarassistant.core.hotword_detector.HotWordDetector
import vn.vistark.oscarassistant.core.Constants
import vn.vistark.oscarassistant.ui.main.MainActivity


class OscarServices : Service() {

    companion object {
        var oscarCurrentVoice: Voice? = null
    }

    val TAG = OscarServices::class.java.simpleName
    val binder = LocalBinder()
    var serviceCallbacks: ServiceCallbacks? = null

    // Phần khai báo liên quan đến thông báo (Notification)
    private val mNotificationChannelId = "setting"
    private val mNotificationId = 12221

    lateinit var hotwordDetector: HotWordDetector

    @SuppressLint("DefaultLocale")
    fun initHotwordDetector() {
        hotwordDetector = HotWordDetector(this, Constants.wakeUpHotKey)
        hotwordDetector.execute()
        hotwordDetector.onDetected = {
            when (it.toUpperCase()) {
                Constants.wakeUpHotKey.toUpperCase() -> {
                    hotwordDetector.stopListen()
                    if (!MainActivity.isActive) {
                        val i = Intent(this, MainActivity::class.java)
                        i.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                        startActivity(i)
                    } else {
                        serviceCallbacks?.triggerCall()
                    }
                }
                Constants.closeHotKey.toUpperCase() -> {
                    if (MainActivity.isActive) {
                        serviceCallbacks?.triggerClose()
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val notiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // A. Tạo notification channel cho android phiên bản từ O đổ lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    mNotificationChannelId,
                    "Cài đặt",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            channel.lightColor = Color.BLUE
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

            notiManager.createNotificationChannel(channel)
        }

        // B. Tạo pendingIntent cho notify
        val intentHome = Intent(this, MainActivity::class.java)
        intentHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(this, 0, intentHome, 0)

        // C. Hiển thị noti và chạy services ngầm
        val notification: Notification = NotificationCompat.Builder(this, mNotificationChannelId)
            .setContentTitle("Oscar Assistance")
            .setContentText("Tôi là bạn của bạn")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notiManager.notify(mNotificationId, notification)

        // D. Tiến hành chạy Forefround (chạy dưới nền)
        startForeground(mNotificationId, notification)

        initHotwordDetector()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    inner class LocalBinder : Binder() {
        val services: OscarServices
            get() = this@OscarServices
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    fun setCallback(callbacks: ServiceCallbacks?) {
        serviceCallbacks = callbacks
    }
}
