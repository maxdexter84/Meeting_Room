package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core_module.event_time_validation.TimeValidationDialogManager
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.data.GagForRooms

@Suppress("UNCHECKED_CAST")
class ModifyUpcomingEventViewModelFactory(
    private val gagForRooms: GagForRooms,
    private val dialogManager: TimeValidationDialogManager
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModifyUpcomingEventViewModel::class.java)) {
            return ModifyUpcomingEventViewModel(
                gagForRooms,
                dialogManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
