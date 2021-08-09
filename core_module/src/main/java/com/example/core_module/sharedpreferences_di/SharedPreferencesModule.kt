package com.example.core_module.sharedpreferences_di

import android.content.Context
import com.example.core_module.sharedpreferences.IPreferenceHelper
import com.example.core_module.sharedpreferences.SharedPreferencesHelper
import com.example.core_module.sharedpreferences.save_data.ISaveData
import com.example.core_module.sharedpreferences.save_data.SaveData
import dagger.Module
import dagger.Provides

@Module
class SharedPreferencesModule(private val context: Context) {
    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideSharedPreferencesHelper(context: Context): IPreferenceHelper =
        SharedPreferencesHelper(context)

    @Provides
    fun provideSaveNetworkData(iPreferenceHelper: IPreferenceHelper): ISaveData =
        SaveData(iPreferenceHelper)
}