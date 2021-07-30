package com.meetingroom.feature_login.di

import com.meetingroom.feature_login.LoginFragment
import com.meetingroom.feature_login.LoginFragmentViewModel
import dagger.Component

@Component(modules = [LoginFragmentModule::class])
@Screen
interface LoginComponent {

    fun provideViewModel(): LoginFragmentViewModel

    fun inject(loginFragment: LoginFragment)
}