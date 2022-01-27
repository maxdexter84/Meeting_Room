package com.andersen.feature_rooms_screen.presentation.di

import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.example.core_network.RequestMaker
import retrofit2.Retrofit

interface RoomScreenDep {
    fun userDataPrefHelper(): UserDataPrefHelper
    fun requestMaker(): RequestMaker
    fun retrofit(): Retrofit
}