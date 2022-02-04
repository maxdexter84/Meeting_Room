package com.andersen.feature_rooms_screen.presentation.di

import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper

interface RoomsScreenDeps {

    fun userDataPrefHelper(): UserDataPrefHelper
}