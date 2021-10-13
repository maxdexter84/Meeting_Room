package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.TimeValidationDialogManager
import kotlinx.coroutines.launch

class ModifyUpcomingEventViewModel(val dialogManager: TimeValidationDialogManager): ViewModel() {

    val effectLiveData = dialogManager.effect.asLiveData()
    val stateLiveData = dialogManager.state.asLiveData()

    fun setEvent(event : TimeValidationDialogManager.ValidationEvent) {
        viewModelScope.launch { dialogManager.handleEvent(event) }
    }
}
