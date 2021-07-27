package com.meetingroom.android

import android.app.Application
import com.meetingroom.android.di.*

class ApplicationMeetingRoom : Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent =
            DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    }
}