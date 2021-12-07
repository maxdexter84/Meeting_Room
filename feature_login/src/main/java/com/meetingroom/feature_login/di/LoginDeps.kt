package com.meetingroom.feature_login.di

import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelper
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.example.core_network.RequestMaker

interface LoginDeps {

    fun userDataPrefHelper(): UserDataPrefHelper
    fun requestMaker(): RequestMaker
}
