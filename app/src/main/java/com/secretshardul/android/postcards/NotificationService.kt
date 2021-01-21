package com.secretshardul.android.postcards

import android.R
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber


class NotificationService: FirebaseMessagingService() {
//    val NOTIFICATION_SOUND_URI=
//        Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.telegraph)

    val VIBRATE_PATTERN = longArrayOf(0, 500)

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.d("Got message: $message")
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        Timber.d("Got new token $newToken")

        val notification = NotificationCompat.Builder(baseContext)
            .setVibrate(VIBRATE_PATTERN)
            .build()

        val notificationManager = baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }
}
