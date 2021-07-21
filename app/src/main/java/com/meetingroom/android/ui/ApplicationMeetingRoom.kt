package com.meetingroom.android.ui

import android.app.Application
import dagger.Component

@Component
interface ApplicationComponent {
//some methods like fun inject(variable: Class)
}

class ApplicationMeetingRoom : Application() {
    val appComponent: ApplicationComponent = DaggerApplicationComponent.create()
}