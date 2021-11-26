package com.meetingroom.android.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.meetingroom.android.R
import com.meetingroom.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        binding.bottomNavView.mainBottomNavigationView.setupWithNavController(navController)
        destinationListener(binding)

    }

    private fun destinationListener(binding: ActivityMainBinding) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (checkDestination(destination)) {
                binding.bottomNavView.mainBottomNavigationView.visibility = View.INVISIBLE
            } else binding.bottomNavView.mainBottomNavigationView.visibility = View.VISIBLE
        }
    }

    private fun checkDestination(destination: NavDestination): Boolean {
        return when {
            destination.hasDeepLink(Uri.parse(resources.getString(R.string.deeplink_uri_set_locations_screen))) ||
                    destination.hasDeepLink(Uri.parse(resources.getString(R.string.deeplink_uri_login_screen))) -> true
            else -> false
        }
    }

}