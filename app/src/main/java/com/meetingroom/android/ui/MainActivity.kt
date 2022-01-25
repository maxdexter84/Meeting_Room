package com.meetingroom.android.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.NavDestination
import androidx.navigation.ui.setupWithNavController
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.deeplink_manager.DeeplinkNavigatorHelper
import com.example.core_module.sharedpreferences.SharedPreferencesKeys
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.meeringroom.ui.view.base_classes.BaseActivity
import com.meetingroom.android.R
import com.meetingroom.android.databinding.ActivityMainBinding
import com.meetingroom.android.di.ApplicationComponent
import java.util.Calendar
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding>({ActivityMainBinding.inflate(it)}, R.id.nav_host_fragment) {

    @Inject
    lateinit var userDataPrefHelper: UserDataPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager
            .findComponent<ApplicationComponent>()
            .inject(this)
        binding.bottomNavView.mainBottomNavigationView.setupWithNavController(navController)
        navigate()
        destinationListener(binding)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navController.popBackStack()
        if(navController.currentDestination?.let { checkDestination(it) } == true) finish()
    }

    private fun navigate() {
        checkAccessTokenForUser()
        val accessToken = userDataPrefHelper.getToken(SharedPreferencesKeys.ACCESS_TOKEN_KEY).toString()
        if (accessToken.isNotBlank()) {
            deeplinkNavigatorHelper.navigate(DeeplinkNavigatorHelper.GO_TO_MY_SPACE)
        } else {
            deeplinkNavigatorHelper.navigate(DeeplinkNavigatorHelper.GO_TO_LOGIN_SCREEN)
        }
    }

    private fun checkAccessTokenForUser(){
        val refreshTime = userDataPrefHelper.getTokenDay()
        if(refreshTime != null) {
            val currentTime: Long = Calendar.getInstance().timeInMillis
            var limitTime = LIMIT_TIME_FOR_ACCESS_TOKEN_FOR_ADMIN - BUFFER_TIME_FOR_REFRESH_ACCESS_TOKEN
            val role = userDataPrefHelper.getUserRole().toString()
            if (role.isNotEmpty() && role == ROLE_USER) {
                limitTime = LIMIT_TIME_FOR_ACCESS_TOKEN_FOR_USER - BUFFER_TIME_FOR_REFRESH_ACCESS_TOKEN
            }
            val elapsedTime = currentTime - refreshTime
            if (elapsedTime > limitTime) {
                userDataPrefHelper.deleteToken()
            }
        }
    }

    private fun destinationListener(binding: ActivityMainBinding) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (checkDestination(destination)) {
                binding.bottomNavView.mainBottomNavigationView.visibility = View.GONE
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
        private const val LIMIT_TIME_FOR_ACCESS_TOKEN_FOR_ADMIN = 86400000
        private const val LIMIT_TIME_FOR_ACCESS_TOKEN_FOR_USER = 604800000
        private const val BUFFER_TIME_FOR_REFRESH_ACCESS_TOKEN = 1000000
        private const val ROLE_USER = "ROLE_USER"
    }

    override fun getViewBinding(): ActivityMainBinding  = ActivityMainBinding.inflate(layoutInflater)
}