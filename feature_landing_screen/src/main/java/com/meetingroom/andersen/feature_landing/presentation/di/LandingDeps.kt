package com.meetingroom.andersen.feature_landing.presentation.di

import com.example.core_module.event_time_validation.TimeValidationDialogManager
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelper
import com.example.core_module.user_logout.LogOutHelper
import com.example.core_network.RequestMaker

interface LandingDeps {
    fun userDataPrefHelper(): UserDataPrefHelper
    fun requestMaker(): RequestMaker
    fun LogOutHelper(): LogOutHelper
    fun timeValidationDialogManager(): TimeValidationDialogManager
}