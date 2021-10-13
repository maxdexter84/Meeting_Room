package com.meetingroom.andersen.feature_landing.time_validation_dialog_manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meetingroom.andersen.feature_landing.room_picker_dialog.presentation.GagForRooms
import com.meetingroom.andersen.feature_landing.room_picker_dialog.presentation.RoomPickerViewModel

@Suppress("UNCHECKED_CAST")
class TimeValidationViewModelFactory() :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimeValidationViewModel::class.java)) {
            return TimeValidationViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}