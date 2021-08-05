package com.meetingroom.feature_login.di

import androidx.lifecycle.ViewModelProvider
import com.example.sharedpreferences.sharedpreferences.IPreferenceHelper
import com.example.sharedpreferences.sharedpreferences.SharedPreferencesHelper
import com.example.sharedpreferences.sharedpreferences.save_data.SaveNetworkData
import com.meetingroom.feature_login.LoginFragment
import com.meetingroom.feature_login.LoginFragmentViewModel
import com.meetingroom.feature_login.LoginViewModelFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class LoginFragmentModule(private val loginFragment: LoginFragment) {

    @Provides
    @Screen
    fun provideViewModelFactory(
        retrofit: Retrofit,
        saveNetworkData: SaveNetworkData
    ): LoginViewModelFactory = LoginViewModelFactory(retrofit, saveNetworkData)

    @Provides
    @Screen
    fun provideLoginViewModel(loginViewModelFactory: LoginViewModelFactory): LoginFragmentViewModel =
        ViewModelProvider(
            loginFragment,
            loginViewModelFactory
        ).get(LoginFragmentViewModel::class.java)
}