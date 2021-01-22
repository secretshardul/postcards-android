package com.secretshardul.android.postcards

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation)

        Timber.d("Activity created")

        bottomNavigation.setOnNavigationItemSelectedListener {
            Timber.d("New item selected: ${it.title}")
            val fragmentToOpen = when (it.itemId) {
                R.id.navigation_code_setup -> CodeFragment()
                else -> HomeFragment()
            }
            openFragment(fragmentToOpen)
            return@setOnNavigationItemSelectedListener true
        }
        bottomNavigation.selectedItemId = R.id.navigation_postcards

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        // Save token to Firestore, if not already done
        CoroutineScope(Dispatchers.IO).launch {
            val savedDocumentId = sharedPref.getString(DOCUMENT_ID_KEY, null)
            val token = Firebase.messaging.token.await()
            if (savedDocumentId == null) {
                try {
                    Timber.d("Got token: $token")
                    val usersCollection = Firebase.firestore.collection("users")
                    val newDocumentId = usersCollection.document().id

                    Timber.d("Got ID: $newDocumentId")
                    usersCollection.document(newDocumentId).set(
                        hashMapOf(
                            "fcmToken" to token
                        )
                    ).await()

                    sharedPref.edit()
                        .putString(DOCUMENT_ID_KEY, newDocumentId)
                        .apply()

                } catch (exception: Exception) {
                    Timber.e(exception)
                }
            }
        }

        // Notification channel with custom sound
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val sound = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + baseContext.packageName + "/" + R.raw.office_phone
            )
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            val channel = NotificationChannel(
                baseContext.getString(R.string.default_notification_channel_id),
                "Default alarm channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Notification with long sound alert"
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.setSound(sound, audioAttributes)

            val notificationManager = getSystemService(
                NotificationManager::class.java
            ) as NotificationManager
            Timber.d("Existing channels: ${notificationManager.notificationChannels}")
            notificationManager.createNotificationChannel(channel)

            Timber.d("Channel created")
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        const val DOCUMENT_ID_KEY = "DOCUMENT-ID"
    }
}
