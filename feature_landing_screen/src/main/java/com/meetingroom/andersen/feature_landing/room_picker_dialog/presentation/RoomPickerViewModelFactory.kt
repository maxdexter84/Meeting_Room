package com.meetingroom.andersen.feature_landing.room_picker_dialog.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class RoomPickerViewModelFactory(
    private val gagForRooms: GagForRooms,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomPickerViewModel::class.java)) {
            return RoomPickerViewModel(
                gagForRooms,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}