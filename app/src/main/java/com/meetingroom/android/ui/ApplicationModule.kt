package com.meetingroom.android.ui

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private var application: Application) {

    @Provides
    fun getContext(): Context {
        return application
    }
}