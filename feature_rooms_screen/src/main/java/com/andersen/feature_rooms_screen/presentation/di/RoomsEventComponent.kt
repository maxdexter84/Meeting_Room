package com.andersen.feature_rooms_screen.presentation.di

import android.content.Context
import com.andersen.feature_rooms_screen.presentation.di.view_model.ViewModelModule
import com.andersen.feature_rooms_screen.presentation.new_event.NewEventFragment
import com.meeringroom.ui.event_dialogs.dialog_room_picker.presentation.RoomPickerDialogFragment
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.dialog_rooms.DialogRoomsFragment
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.RoomsEventGridFragment
import com.example.core_module.di.FeatureScope
import dagger.BindsInstance
import dagger.Component

@FeatureScope
@Component(modules = [
    NetworkModule::class,
    ViewModelModule::class,
    NewEventModule::class
])

interface RoomsEventComponent {
    fun inject(roomsEventGridFragment: RoomsEventGridFragment)
    fun inject(newEventFragment: NewEventFragment)
    fun inject(roomsDialogFragment: DialogRoomsFragment)
    fun inject(roomPickerDialogRoomsFragment: RoomPickerDialogFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): RoomsEventComponent
    }
}
