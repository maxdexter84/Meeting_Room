package com.meetingroom.android.di

import android.app.Application
import android.content.Context
import com.meetingroom.android.di.save_data.SaveNetworkData
import com.meetingroom.android.sharedpreferences.IPreferenceHelper
import com.meetingroom.android.sharedpreferences.SharedPreferencesHelper
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