package com.meetingroom.feature_workplaces_screen.presentation.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meetingroom.feature_workplaces_screen.presentation.fragments.book_workplace_fragment.BookWorkplaceViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BookWorkplaceViewModel::class)
    fun bindBookWorkplaceViewModel(bookWorkPlaceViewModel: BookWorkplaceViewModel): ViewModel
}