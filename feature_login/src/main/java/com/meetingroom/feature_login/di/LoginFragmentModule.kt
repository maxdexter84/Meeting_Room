package com.meetingroom.feature_login.di

import androidx.lifecycle.ViewModelProvider
import com.meetingroom.feature_login.LoginFragment
import com.meetingroom.feature_login.LoginFragmentViewModel
import com.meetingroom.feature_login.LoginViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class LoginFragmentModule(private val loginFragment: LoginFragment) {

    @Provides
    @ScreenScope
    fun provideViewModelFactory(): LoginViewModelFactory = LoginViewModelFactory()

    @Provides
    @ScreenScope
    fun provideLoginViewModel(loginViewModelFactory: LoginViewModelFactory): LoginFragmentViewModel =
        ViewModelProvider(loginFragment, loginViewModelFactory).get(LoginFragmentViewModel::class.java)
}