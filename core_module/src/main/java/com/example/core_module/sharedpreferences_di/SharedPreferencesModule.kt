package com.example.core_module.sharedpreferences_di

import com.example.core_module.sharedpreferences.IPreferenceHelper
import com.example.core_module.sharedpreferences.SharedPreferencesHelper
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelper
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import dagger.Binds
import dagger.Module

@Module
interface SharedPreferencesModule {

    @Binds
    fun provideSharedPreferencesHelper(sharedPreferencesHelper : SharedPreferencesHelper): IPreferenceHelper

    @Binds
    fun provideSaveNetworkData(userDataPrefHelperImpl : UserDataPrefHelperImpl): UserDataPrefHelper
}