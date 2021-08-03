package com.example.sharedpreferences

import com.example.sharedpreferences.sharedpreferences.IPreferenceHelper
import com.example.sharedpreferences.sharedpreferences.save_data.SaveNetworkData
import dagger.Component

@Component(modules = [SharedPreferencesModule::class])
interface SharedPreferencesComponent {
    fun sharedPref(): IPreferenceHelper
    fun saveNetworkData(): SaveNetworkData
}