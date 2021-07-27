package com.meetingroom.android

import android.app.Application
import com.meetingroom.android.di.CoreComponent
import com.meetingroom.android.di.CoreModule
import com.meetingroom.android.di.DaggerCoreComponent

class ApplicationMeetingRoom : Application() {
    private lateinit var appComponent: CoreComponent

    override fun onCreate() {
        super.onCreate()
        appComponent =
            DaggerCoreComponent.builder().coreModule(CoreModule(this)).build()
    }

}