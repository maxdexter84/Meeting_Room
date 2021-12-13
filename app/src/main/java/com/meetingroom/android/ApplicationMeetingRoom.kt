package com.meetingroom.android

import android.app.Application
import com.example.core_module.component_manager.IHasComponent
import com.example.core_module.component_manager.XInjectionManager
import com.meetingroom.android.di.ApplicationComponent
import com.meetingroom.android.di.DaggerApplicationComponent

class ApplicationMeetingRoom : Application(), IHasComponent<ApplicationComponent> {

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    fun initDagger() {
        XInjectionManager.init(this)
        XInjectionManager.bindComponent(this)
    }

    override fun getComponent(): ApplicationComponent = DaggerApplicationComponent
        .factory()
        .create(this)

}