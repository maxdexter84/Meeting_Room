package com.meetingroom.android.ui

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private var application: ApplicationMeetingRoom) {

    @Provides
    fun getContext(): Context {
        return application
    }
}