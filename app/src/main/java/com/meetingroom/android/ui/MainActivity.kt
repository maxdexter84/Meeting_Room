package com.meetingroom.android.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.sharedpreferences.SharedPreferencesKeys
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.meetingroom.android.R
import com.meetingroom.android.databinding.ActivityMainBinding
import com.meetingroom.android.di.ApplicationComponent
import java.util.Calendar
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    @Inject
    lateinit var userDataPrefHelper: UserDataPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager
            .findComponent<ApplicationComponent>()
            .inject(this)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        binding.bottomNavView.mainBottomNavigationView.setupWithNavController(navController)
        navigate()
        destinationListener(binding)
    }

    private fun navigate() {
        checkAccessTokenForUser()
        val accessToken = userDataPrefHelper.getToken(SharedPreferencesKeys.ACCESS_TOKEN_KEY).toString()
        val uri = if (accessToken.isNotBlank()) {
            Uri.parse(resources.getString(R.string.deeplink_uri_my_space_fragment))
        } else {
            Uri.parse(resources.getString(R.string.deeplink_uri_login_screen))
        }
        with(navController){
            popBackStack(R.id.login_fragment, true)
            navigate(uri)
        }
    }

    private fun checkAccessTokenForUser(){
        val refreshTime = userDataPrefHelper.getTokenDay()
        val currentTime: Long = Calendar.getInstance().timeInMillis
        val limitTime = LIMIT_TIME_FOR_ACCESS_TOKEN_FOR_USER - BUFFER_TIME_FOR_REFRESH_ACCESS_TOKEN
        if(refreshTime != null) {
            val elapsedTime = currentTime - refreshTime
            if (elapsedTime > limitTime) {
                userDataPrefHelper.deleteToken()
            }
        }
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

    companion object {
        private const val LIMIT_TIME_FOR_ACCESS_TOKEN_FOR_USER = 604800000
        private const val BUFFER_TIME_FOR_REFRESH_ACCESS_TOKEN = 10000
    }
}