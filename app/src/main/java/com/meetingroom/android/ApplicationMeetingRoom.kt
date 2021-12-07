package com.meetingroom.android

import android.app.Application
import com.meetingroom.android.di.ApplicationComponent
import com.meetingroom.android.di.DaggerApplicationComponent
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager

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