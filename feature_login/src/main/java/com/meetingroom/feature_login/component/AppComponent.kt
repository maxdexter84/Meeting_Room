package com.meetingroom.feature_login.component

import android.content.Context
import com.meetingroom.feature_login.LoginFragmentComponent
import com.meetingroom.feature_login.modules.AppModule
import com.meetingroom.feature_login.modules.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {

    val context: Context

    val loginFragmentComponent: LoginFragmentComponent
}