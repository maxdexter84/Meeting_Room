package com.meetingroom.andersen.feature_landing.di.room_picker_fragment

import androidx.lifecycle.ViewModelProvider
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.room_picker_dialog.presentation.GagForRooms
import com.meetingroom.andersen.feature_landing.room_picker_dialog.presentation.RoomPickerViewModel
import com.meetingroom.andersen.feature_landing.room_picker_dialog.presentation.RoomPickerViewModelFactory
import com.meetingroom.andersen.feature_landing.room_picker_dialog.ui.RoomPickerDialogFragment
import dagger.Module
import dagger.Provides

@Module
class RoomPickerModule(private val roomPickerDialogFragment: RoomPickerDialogFragment) {

    @Provides
    @Screen
    fun provideGagForRooms() = GagForRooms()

    @Provides
    @Screen
    fun provideViewModelFactory(
        gagForRooms: GagForRooms,
        saveData: UserDataPrefHelperImpl
    ): RoomPickerViewModelFactory =
        RoomPickerViewModelFactory(gagForRooms, saveData)

    @Provides
    @Screen
    fun provideViewModel(roomPickerViewModelFactory: RoomPickerViewModelFactory): RoomPickerViewModel {
        return ViewModelProvider(
            roomPickerDialogFragment,
            roomPickerViewModelFactory
        ).get(RoomPickerViewModel::class.java)
    }
}