package com.meetingroom.feature_login

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides

@Module
class LoginFragmentModule(private val loginFragment: LoginFragment) {

    @Provides
    fun provideViewModelFactory(): LoginViewModelFactory = LoginViewModelFactory()

    @Provides
    fun provideLoginViewModel(loginViewModelFactory: LoginViewModelFactory): LoginFragmentViewModel =
        ViewModelProvider(loginFragment, loginViewModelFactory).get(LoginFragmentViewModel::class.java)
}