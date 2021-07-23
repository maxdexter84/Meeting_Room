package com.meetingroom.android

import android.app.Application
import com.meetingroom.android.di.ApplicationComponent
import com.meetingroom.android.di.DaggerApplicationComponent

class ApplicationMeetingRoom : Application() {
    val appComponent: ApplicationComponent = DaggerApplicationComponent.create()

}