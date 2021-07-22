package com.meetingroom.android.ui

import android.app.Application

class ApplicationMeetingRoom : Application() {
    val appComponent: ApplicationComponent = DaggerApplicationComponent.create()

}