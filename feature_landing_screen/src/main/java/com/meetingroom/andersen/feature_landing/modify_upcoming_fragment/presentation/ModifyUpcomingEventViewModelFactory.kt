package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl

@Suppress("UNCHECKED_CAST")
class ModifyUpcomingEventViewModelFactory() :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModifyUpcomingEventViewModel::class.java)) {
            return ModifyUpcomingEventViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}