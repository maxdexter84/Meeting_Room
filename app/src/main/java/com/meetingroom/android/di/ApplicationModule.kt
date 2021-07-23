package com.meetingroom.android.di

import android.content.Context
import com.meetingroom.android.ApplicationMeetingRoom
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private var application: ApplicationMeetingRoom) {

    @Provides
    fun getContext(): Context {
        return application
    }
}