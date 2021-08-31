package com.meetingroom.andersen.feature_landing_screen.landing_fragment

import androidx.lifecycle.ViewModel
import com.example.core_module.user_logout.LogOutHelper

class LandingFragmentViewModel(private val logOutHelper: LogOutHelper) : ViewModel() {

    fun logout() {
        logOutHelper.logout()
    }

    fun isDeleteRequired() {
        logOutHelper.isDeleteRequired()
    }
}