package com.secretshardul.android.postcards

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class NotificationService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.d("Got message: $message")
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        Timber.d("Got new token $newToken")
    }
}
