package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ModifyUpcomingEventViewModel(private val dialogManager: TimeValidationDialogManager): ViewModel() {

    val effectLiveData = dialogManager.effect
    val stateLiveData = dialogManager.state

    fun setEvent(event : TimeValidationDialogManager.ValidationEvent) {
        viewModelScope.launch { dialogManager.handleEvent(event) }
    }
}
