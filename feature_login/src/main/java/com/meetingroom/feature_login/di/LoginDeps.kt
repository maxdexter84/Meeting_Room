package com.meetingroom.feature_login.di

import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.example.core_network.RequestMaker
import retrofit2.Retrofit

interface LoginDeps {

    fun userDataPrefHelper(): UserDataPrefHelper
    fun requestMaker(): RequestMaker
    fun retrofit(): Retrofit
}
