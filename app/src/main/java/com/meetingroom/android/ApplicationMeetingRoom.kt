package com.meetingroom.android

import android.app.Application
import com.example.core_network.NetworkModule
import com.meetingroom.android.di.AppComponentProvider
import com.meetingroom.android.di.ApplicationComponent
import com.meetingroom.android.di.ApplicationModule
import com.meetingroom.android.di.DaggerApplicationComponent


class ApplicationMeetingRoom : Application(), AppComponentProvider {
    private lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        initComponent()
    }

    private fun initComponent() {
        appComponent =
            DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this))
                .networkModule(
                    NetworkModule()
                ).build()
    }

    override fun provideCoreComponent(): ApplicationComponent {
        if (this::appComponent.isInitialized.not()) {
            initComponent()
        }
        return appComponent
    }
}