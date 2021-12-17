package com.example.core_module.di

import com.example.core_module.sharedpreferences.SharedPreferencesHelper
import com.example.core_module.sharedpreferences.pref_helper.IPreferenceHelper
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelperImpl
import dagger.Binds
import dagger.Module

@Module
interface SharedPreferencesModule {

    @Binds
    fun provideSharedPreferencesHelper(sharedPreferencesHelper : SharedPreferencesHelper): IPreferenceHelper

    @Binds
    fun provideSaveNetworkData(userDataPrefHelperImpl : UserDataPrefHelperImpl): UserDataPrefHelper
}