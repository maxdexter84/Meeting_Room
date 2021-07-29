package com.meetingroom.android.di

import com.example.core_network.NetworkModule
import com.meetingroom.android.ui.MainActivity
import dagger.Component

@Component(modules = [ApplicationModule::class, NetworkModule::class])
interface ApplicationComponent {
     fun inject(mainActivity: MainActivity)

}