package com.meetingroom.andersen.feature_landing.presentation.di

import com.example.core_module.event_time_validation.AdminTimeValidationDialogManager
import com.example.core_module.event_time_validation.UserTimeValidationDialogManager
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.example.core_module.user_logout.LogOutHelper
import com.example.core_network.RequestMaker
import retrofit2.Retrofit

interface LandingDeps {
    fun userDataPrefHelper(): UserDataPrefHelper
    fun LogOutHelper(): LogOutHelper
    fun userTimeValidationDialogManager(): UserTimeValidationDialogManager
    fun adminTimeValidationDialogManager(): AdminTimeValidationDialogManager
    fun requestMaker(): RequestMaker
    fun retrofit(): Retrofit
}