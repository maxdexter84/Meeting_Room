package com.meetingroom.feature_meet_now.presentation.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meetingroom.feature_meet_now.presentation.viewmodel.MeetNowSharedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MeetNowSharedViewModel::class)
    fun bindMeetNowSharedViewModel(viewModel: MeetNowSharedViewModel): ViewModel
}