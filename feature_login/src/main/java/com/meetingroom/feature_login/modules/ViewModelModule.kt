package com.meetingroom.feature_login.modules

import androidx.lifecycle.ViewModelProvider
import com.meetingroom.feature_login.di.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory) : ViewModelProvider.Factory
}