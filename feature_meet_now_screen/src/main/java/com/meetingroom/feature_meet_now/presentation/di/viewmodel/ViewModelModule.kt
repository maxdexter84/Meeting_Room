package com.meetingroom.feature_meet_now.presentation.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meetingroom.feature_meet_now.presentation.available_now_fragment.RoomsAvailableNowViewModel
import com.meetingroom.feature_meet_now.presentation.available_soon_fragment.RoomsAvailableSoonViewModel
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
    fun bindRoomsAvailableNowViewModel(viewModel: RoomsAvailableNowViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RoomsAvailableSoonViewModel::class)
    fun bindRoomsAvailableSoonViewModel(viewModel: RoomsAvailableSoonViewModel): ViewModel
}