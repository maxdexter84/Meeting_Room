package com.example.core_module.sharedpreferences_di

import com.example.core_module.sharedpreferences.IPreferenceHelper
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelper
import dagger.Component

@Component(modules = [SharedPreferencesModule::class])
interface SharedPreferencesComponent {
    fun sharedPref(): IPreferenceHelper
    fun userDataPrefHelper(): UserDataPrefHelper
}