package com.meetingroom.feature_meet_now.presentation.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meetingroom.feature_meet_now.presentation.available_now_fragment.RoomsAvailableNowViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RoomsAvailableNowViewModel::class)
    fun bindRoomsAvailableNowViewModel(room: RoomsAvailableNowViewModel): ViewModel
}