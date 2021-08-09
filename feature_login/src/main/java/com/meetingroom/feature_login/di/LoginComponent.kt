package com.meetingroom.feature_login.di

import com.example.core_network.NetworkModule
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meetingroom.feature_login.LoginFragment
import com.meetingroom.feature_login.LoginFragmentViewModel
import dagger.Component

@Component(modules = [LoginFragmentModule::class, NetworkModule::class, SharedPreferencesModule::class])
@Screen
interface LoginComponent {

    fun provideViewModel(): LoginFragmentViewModel

    fun inject(loginFragment: LoginFragment)
}