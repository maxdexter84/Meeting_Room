package com.example.feature_set_location.di

import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelper
import com.example.core_network.RequestMaker

interface SetLocationDeps {
    fun requestMaker(): RequestMaker
    fun userDataPrefHelper(): UserDataPrefHelper
}
