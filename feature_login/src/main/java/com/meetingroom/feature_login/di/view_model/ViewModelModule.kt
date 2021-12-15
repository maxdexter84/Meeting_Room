package com.meetingroom.feature_login.di.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meetingroom.feature_login.LoginFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginFragmentViewModel::class)
    fun bindLoginFragmentViewModel(roomsEventViewModel: LoginFragmentViewModel): ViewModel
}
