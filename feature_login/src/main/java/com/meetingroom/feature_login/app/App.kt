package com.meetingroom.feature_login.app

import android.app.Application
import com.meetingroom.feature_login.di.Injector

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        Injector.initAppComponent(this)
    }
}