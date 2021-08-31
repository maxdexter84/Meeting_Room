package com.meetingroom.andersen.feature_landing.landing_fragment

import androidx.lifecycle.ViewModel
import com.example.core_module.user_logout.LogOutHelper

class LandingFragmentViewModel(private val logOutHelper: LogOutHelper) : ViewModel() {

    init {
        if (logOutHelper.isDeleteRequired()) logOutHelper.logout()
    }

    fun logout() {
        logOutHelper.logout()
    }
}