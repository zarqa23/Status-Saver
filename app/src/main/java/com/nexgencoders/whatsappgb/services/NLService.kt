package com.nexgencoders.whatsappgb.services

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.nexgencoders.whatsappgb.BuildConfig
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.utility.isValidApp
import com.nexgencoders.whatsappgb.utility.isValidTitle

class NLService : NotificationListenerService() {

    private val nlServiceReceiver = NLServiceReceiver()

    override fun onCreate() {
        super.onCreate()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.nexgencoders.whatsappgb")
        registerReceiver(nlServiceReceiver, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn != null && sbn.packageName.isValidApp()) {
            sendMessage(sbn)
        }
    }

    private fun sendMessage(sbn: StatusBarNotification) {
        val intent = Intent("com.nexgencoders.whatsappgb")

        val notification = sbn.notification
        val extras = notification.extras
        val title = extras.getString("android.title")
        val text = extras.getCharSequence("android.text").toString()
        val app = sbn.packageName

        intent.putExtra("time", sbn.notification.`when`)
        intent.putExtra("title", title)
        intent.putExtra("text", text)
        intent.putExtra("app", app)

        if (title != null && title.isValidTitle()) {
            sendBroadcast(intent)
        }

        if (text == getString(R.string.deleted_message) || text == getString(R.string.signal_deleted_message)) {
            intent.putExtra("isDeleted", true)
            intent.putExtra("title", title)
            sendBroadcast(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(nlServiceReceiver)
        } catch (e: IllegalArgumentException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }
}