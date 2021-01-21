package com.secretshardul.android.postcards

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation)

        Timber.d("Activity created")

        bottomNavigation.setOnNavigationItemSelectedListener {
            Timber.d("New item selected: ${it.title}")
            val fragmentToOpen = when(it.itemId) {
                R.id.navigation_code_setup -> CodeFragment()
                R.id.navigation_channels -> ChannelsFragment()
                else -> HomeFragment()
            }
            openFragment(fragmentToOpen)
            return@setOnNavigationItemSelectedListener true
        }
        bottomNavigation.selectedItemId = R.id.navigation_postcards

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
}
