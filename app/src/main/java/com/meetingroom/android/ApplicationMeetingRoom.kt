package com.meetingroom.android

import android.app.Application
import com.meetingroom.android.di.ApplicationComponent
import com.meetingroom.android.di.ApplicationModule
import com.meetingroom.android.di.DaggerApplicationComponent

class ApplicationMeetingRoom : Application() {
    private lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent =
            DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    }

}