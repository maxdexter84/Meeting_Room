package com.andersen.feature_rooms_screen.presentation.di.rooms_dialog

import androidx.lifecycle.ViewModelProvider
import com.andersen.feature_rooms_screen.presentation.dialog_rooms.ui.DialogRoomsFragment
import com.andersen.feature_rooms_screen.presentation.dialog_rooms.presentation.GagForRooms
import com.andersen.feature_rooms_screen.presentation.dialog_rooms.presentation.RoomsViewModel
import com.andersen.feature_rooms_screen.presentation.dialog_rooms.presentation.RoomsViewModelFactory
import com.example.core_module.di.FeatureScope
import dagger.Module
import dagger.Provides

@Module
class RoomsModule(private val dialogRoomsFragment: DialogRoomsFragment) {

    @Provides
    @FeatureScope
    fun provideGagForRooms() = GagForRooms()

    @Provides
    @FeatureScope
    fun provideViewModelFactory(
        gagForRooms: GagForRooms,
    ): RoomsViewModelFactory =
        RoomsViewModelFactory(gagForRooms)

    @Provides
    @FeatureScope
    fun provideViewModel(roomsViewModelFactory: RoomsViewModelFactory): RoomsViewModel {
        return ViewModelProvider(
            dialogRoomsFragment,
            roomsViewModelFactory
        ).get(RoomsViewModel::class.java)
    }
}
