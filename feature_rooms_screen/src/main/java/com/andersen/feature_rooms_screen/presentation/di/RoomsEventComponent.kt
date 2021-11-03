package com.andersen.feature_rooms_screen.presentation.di

import com.andersen.feature_rooms_screen.presentation.di.view_model.ViewModelModule
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.RoomsEventGridFragment
import com.example.core_module.di.FeatureScope
import dagger.Component

@FeatureScope
@Component(modules = [
    NetworkModule::class,
    ViewModelModule::class
])

interface RoomsEventComponent {
    fun inject(roomsEventGridFragment: RoomsEventGridFragment)
}
