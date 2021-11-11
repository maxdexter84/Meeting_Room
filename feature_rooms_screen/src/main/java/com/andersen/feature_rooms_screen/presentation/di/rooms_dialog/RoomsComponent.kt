package com.andersen.feature_rooms_screen.presentation.di.rooms_dialog


import com.andersen.feature_rooms_screen.presentation.dialog_rooms.ui.DialogRoomsFragment
import com.example.core_module.di.FeatureScope
import dagger.Component

@FeatureScope
@Component(modules = [RoomsModule::class])
interface RoomsComponent {
    fun inject(roomsDialogFragment: DialogRoomsFragment)
}
