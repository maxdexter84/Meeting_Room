package com.meetingroom.android.di

import android.app.Application
import android.content.Context
import com.example.sharedpreferences.sharedpreferences.save_data.SaveNetworkData
import com.example.sharedpreferences.sharedpreferences.IPreferenceHelper
import com.example.sharedpreferences.sharedpreferences.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = application

    @Provides
    @Singleton
    fun provideSharedPreferencesHelper(context: Context): IPreferenceHelper =
        SharedPreferencesHelper(context)

    @Provides
    @Singleton
    fun provideSaveNetworkData(iPreferenceHelper: IPreferenceHelper): SaveNetworkData =
       SaveNetworkData(iPreferenceHelper)
}