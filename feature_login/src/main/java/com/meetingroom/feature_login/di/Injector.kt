package com.meetingroom.feature_login.di

import android.content.Context
import com.meetingroom.feature_login.component.AppComponent
import com.meetingroom.feature_login.component.DaggerAppComponent
import com.meetingroom.feature_login.LoginFragmentComponent
import com.meetingroom.feature_login.modules.AppModule

object Injector {

    private lateinit var appComponent: AppComponent

    val loginFragmentComponent: LoginFragmentComponent
        get() {
            return appComponent.loginFragmentComponent
        }

    internal fun initAppComponent(context: Context) {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(context))
            .build()
    }
}