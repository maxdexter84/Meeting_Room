package com.meetingroom.android.di

import android.content.Context
import com.meetingroom.android.di.save_data.SaveNetworkData
import com.meetingroom.android.sharedpreferences.IPreferenceHelper
import com.meetingroom.android.ui.MainActivity
import com.meetingroom.android.ui.SampleActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [CoreModule::class])
@Singleton
interface CoreComponent {
    fun context(): Context
    fun sharedPref(): IPreferenceHelper
    fun inject(activity: MainActivity)
    fun saveNetworkData():SaveNetworkData
}