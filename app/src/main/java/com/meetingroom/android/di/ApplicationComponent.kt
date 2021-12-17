package com.meetingroom.android.di

import android.content.Context
import com.example.core_module.di.SharedPreferencesModule
import com.example.core_network.NetworkModule
import com.example.feature_set_location.di.SetLocationDeps
import com.meetingroom.andersen.feature_landing.presentation.di.LandingDeps
import com.meetingroom.android.ApplicationMeetingRoom
import com.meetingroom.android.ui.MainActivity
import com.meetingroom.feature_login.di.LoginDeps
import dagger.BindsInstance
import dagger.Component

@Component(modules = [SharedPreferencesModule::class, NetworkModule::class])
interface ApplicationComponent:
     LandingDeps,
     LoginDeps,
     SetLocationDeps
{

     fun inject(activity: MainActivity)
     fun inject(application: ApplicationMeetingRoom)

     @Component.Factory
     interface Factory {
          fun create(@BindsInstance context: Context): ApplicationComponent
     }
}