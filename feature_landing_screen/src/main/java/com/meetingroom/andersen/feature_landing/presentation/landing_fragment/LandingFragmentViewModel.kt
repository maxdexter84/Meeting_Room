package com.meetingroom.andersen.feature_landing.presentation.landing_fragment

import androidx.lifecycle.ViewModel
import com.example.core_module.user_logout.LogOutHelper
import javax.inject.Inject

class LandingFragmentViewModel @Inject constructor(private val logOutHelper: LogOutHelper) : ViewModel() {

    init {
        if (logOutHelper.isDeleteRequired()) logOutHelper.logout()
    }

    fun logout() {
        logOutHelper.logout()
    }
}