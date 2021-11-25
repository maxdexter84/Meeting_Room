package com.meetingroom.android.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.meetingroom.android.R
import com.meetingroom.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        binding.bottomNavView.mainBottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val uri =
                Uri.parse(resources.getString(com.meetingroom.ui.R.string.deeplink_uri_set_locations_screen))
            if (destination.hasDeepLink(uri)) {
                binding.bottomNavView.mainBottomNavigationView.visibility = View.INVISIBLE
            } else binding.bottomNavView.mainBottomNavigationView.visibility = View.VISIBLE
        }
        navController.currentDestination

    }
}