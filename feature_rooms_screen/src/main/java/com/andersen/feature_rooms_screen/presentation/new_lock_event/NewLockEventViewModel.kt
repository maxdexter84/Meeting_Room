package com.andersen.feature_rooms_screen.presentation.new_lock_event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.event_time_validation.AdminTimeValidationDialogManager
import com.example.core_module.event_time_validation.TimeValidationEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewLockEventViewModel @Inject constructor(
    private val dialogManager: AdminTimeValidationDialogManager
): ViewModel() {

    val dateStateLiveData = dialogManager.dateState
    val effectLiveData = dialogManager.effect
    val stateLiveData = dialogManager.state

    fun setEvent(event: TimeValidationEvent.AdminTimeValidationEvent) {
        viewModelScope.launch { dialogManager.handleEvent(event) }
    }
}