package com.meetingroom.feature_login

import dagger.Component

@Component(modules = [LoginFragmentModule::class])
interface LoginComponent {

    fun provideViewModel(): LoginFragmentViewModel

    fun inject(loginFragment: LoginFragment)
}