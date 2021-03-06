package com.andersen.feature_rooms_screen.presentation.di.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andersen.feature_rooms_screen.presentation.RoomsEventViewModel
import com.andersen.feature_rooms_screen.presentation.new_lock_event.NewLockEventViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RoomsEventViewModel::class)
    fun bindRoomsEventViewModel(roomsEventViewModel: RoomsEventViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewLockEventViewModel::class)
    fun bindNewLockEventViewModel(newLockEventViewModel: NewLockEventViewModel): ViewModel
}
