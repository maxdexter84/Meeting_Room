package com.meetingroom.android.di

import android.content.Context
import com.meetingroom.android.sharedpreferences.SharedPreferencesHelper
import com.meetingroom.android.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [CoreModule::class])
@Singleton
interface CoreComponent {

    fun context(): Context
    fun sharedPref(): SharedPreferencesHelper
    fun inject(activity: MainActivity)
}