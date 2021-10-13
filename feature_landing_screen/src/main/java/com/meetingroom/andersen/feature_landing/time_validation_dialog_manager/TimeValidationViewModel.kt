package com.meetingroom.andersen.feature_landing.time_validation_dialog_manager

import androidx.lifecycle.asLiveData

class TimeValidationViewModel: BaseViewModel<TimeValidationDialogManager.Event, TimeValidationDialogManager.State, TimeValidationDialogManager.Effect>() {

    private val dialogManager = TimeValidationDialogManager()
    val effectLiveData = effect.asLiveData()
    val stateLiveData = uiState.asLiveData()

    override fun createInitialState(): TimeValidationDialogManager.State {
        return TimeValidationDialogManager.State(
            TimeValidationDialogManager.ValidationState.TimeIsValid
        )
    }

    override fun handleEvent(event: TimeValidationDialogManager.Event) {
        setEffect { dialogManager.getEffect(event) }
        setState { dialogManager.getState() }
    }
}
