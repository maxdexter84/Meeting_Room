package com.meetingroom.feature_login.di

import androidx.lifecycle.ViewModelProvider
import com.example.sharedpreferences.sharedpreferences.save_data.SaveNetworkData
import com.meetingroom.feature_login.LoginFragment
import com.meetingroom.feature_login.LoginFragmentViewModel
import com.meetingroom.feature_login.LoginViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class LoginFragmentModule(private val loginFragment: LoginFragment) {

    @Provides
    @Screen
    fun provideViewModelFactory(
        saveNetworkData: SaveNetworkData
    ): LoginViewModelFactory = LoginViewModelFactory( saveNetworkData)

    @Provides
    @Screen
    fun provideLoginViewModel(loginViewModelFactory: LoginViewModelFactory): LoginFragmentViewModel =
        ViewModelProvider(
            loginFragment,
            loginViewModelFactory
        ).get(LoginFragmentViewModel::class.java)
}