package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ModifyUpcomingEventViewModelFactory(
    private val dialogManager: TimeValidationDialogManager
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModifyUpcomingEventViewModel::class.java)) {
            return ModifyUpcomingEventViewModel(
                dialogManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
