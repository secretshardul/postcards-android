package com.secretshardul.android.postcards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
