package com.meetingroom.android.ui

import android.app.Application
import android.content.Context
import javax.inject.Singleton

class ApplicationMeetingRoom : Application() {
    val appComponent: ApplicationComponent = DaggerApplicationComponent.create()

    @Singleton
    fun getContext(): Context {
        return appComponent as Context
    }
}