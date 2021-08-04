package com.example.sharedpreferences

import android.content.Context
import com.example.sharedpreferences.sharedpreferences.IPreferenceHelper
import com.example.sharedpreferences.sharedpreferences.SharedPreferencesHelper
import com.example.sharedpreferences.sharedpreferences.save_data.ISaveNetworkData
import com.example.sharedpreferences.sharedpreferences.save_data.SaveNetworkData
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
    fun provideSaveNetworkData(iPreferenceHelper: IPreferenceHelper): ISaveNetworkData =
        SaveNetworkData(iPreferenceHelper)
}