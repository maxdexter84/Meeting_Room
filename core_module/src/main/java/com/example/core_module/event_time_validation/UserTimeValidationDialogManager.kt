package com.example.core_module.event_time_validation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.core_module.R
import com.example.core_module.event_time_validation.TimeValidationDialogManagerInterface.Companion.MAX_HOURS_DIFF
import com.example.core_module.event_time_validation.TimeValidationDialogManagerInterface.Companion.MAX_TIME
import com.example.core_module.event_time_validation.TimeValidationDialogManagerInterface.Companion.MIN_MINUTES_DIFF
import com.example.core_module.event_time_validation.TimeValidationDialogManagerInterface.Companion.MIN_TIME
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class UserTimeValidationDialogManager @Inject constructor(): TimeValidationDialogManagerInterface {

    private val _state = MutableLiveData<TimeValidationState>()
    override val state: LiveData<TimeValidationState> = _state

    private val _effect = MutableLiveData<TimeValidationEffect>()
    override val effect: LiveData<TimeValidationEffect> = _effect

    override fun handleEvent(event: TimeValidationEvent) {
        if (event is TimeValidationEvent.UserTimeValidationEvent) {
            handleUserEvent(event)
        }
    }

    private fun handleUserEvent(event: TimeValidationEvent.UserTimeValidationEvent) {
        _effect.value = when (event) {
            is TimeValidationEvent.UserTimeValidationEvent.OnStartTimeChanged -> validateStartTime(event.startTime, event.endTime, event.date)
            is TimeValidationEvent.UserTimeValidationEvent.OnEndTimeChanged -> validateEndTime(event.startTime, event.endTime, event.date)
            is TimeValidationEvent.UserTimeValidationEvent.OnDateChanged -> validateDate(event.startTime, event.date)
        }
    }

    private fun isTimeNotInOfficeHours(startTime: LocalTime, endTime: LocalTime): Boolean {
        return ((startTime.isBefore(MIN_TIME) || startTime.isAfter(MAX_TIME))
                && (endTime.isBefore(MIN_TIME) || endTime.isAfter(MAX_TIME)))
    }

    override fun validateBothTimes(startTime: LocalTime, endTime: LocalTime): TimeValidationEffect {
        return when {
            endTime.isBefore(startTime) -> {
                _state.value = TimeValidationState.InvalidEndTime
                TimeValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_end_before_it_starts_message)
            }
            endTime.minusHours(startTime.hour.toLong()).minusMinutes(startTime.minute.toLong()).isAfter(LocalTime.of(
                MAX_HOURS_DIFF, 0)) -> {
                _state.value = TimeValidationState.InvalidBothTime
                TimeValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_last_longer_than_4_hours_message)
            }
            endTime.isBefore(startTime.plusMinutes(MIN_MINUTES_DIFF)) -> {
                _state.value = TimeValidationState.InvalidBothTime
                TimeValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_last_less_than_15_minutes_message)
            }
            else -> {
                _state.value = TimeValidationState.TimeIsValid
                TimeValidationEffect.TimeIsValidEffect
            }
        }
    }

    override fun validateStartTime(startTime: LocalTime, endTime: LocalTime, date: LocalDate): TimeValidationEffect {
        return when {
            isStartTimeBeforeCurrent(startTime, date) -> {
                _state.value = TimeValidationState.InvalidStartTime
                TimeValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_start_before_current_time_message)
            }
            isTimeNotInOfficeHours(startTime, endTime) -> {
                _state.value = TimeValidationState.InvalidBothTime
                TimeValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_start_and_end_between_0_and_6_hours_message)
            }
            startTime.isBefore(MIN_TIME) || startTime.isAfter(MAX_TIME) -> {
                _state.value = TimeValidationState.InvalidStartTime
                TimeValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_start_between_0_and_6_hours_message)
            }
            else -> {
                if (_state.value != TimeValidationState.InvalidEndTime) {
                    validateBothTimes(startTime, endTime)
                } else {
                    validateEndTime(startTime, endTime, date)
                }
            }
        }
    }

     override fun validateEndTime(startTime: LocalTime, endTime: LocalTime, date: LocalDate): TimeValidationEffect {
        return when {
            isTimeNotInOfficeHours(startTime, endTime) -> {
                _state.value = TimeValidationState.InvalidBothTime
                TimeValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_start_and_end_between_0_and_6_hours_message)
            }
            endTime.isBefore(MIN_TIME) || endTime.isAfter(MAX_TIME) -> {
                _state.value = TimeValidationState.InvalidEndTime
                TimeValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_end_between_0_and_6_hours_message)
            }
            else -> {
                if (_state.value != TimeValidationState.InvalidStartTime &&
                    _state.value != TimeValidationState.InvalidBothTime) {
                    validateBothTimes(startTime, endTime)
                } else {
                    validateStartTime(startTime, endTime, date)
                }
            }
        }
    }

    fun validateDate(startTime: LocalTime, date: LocalDate): TimeValidationEffect =
        if (isStartTimeBeforeCurrent(startTime, date)) {
            _state.value = TimeValidationState.InvalidStartTime
            TimeValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_start_before_current_time_message)
        } else {
            TimeValidationEffect.TimeIsValidEffect
        }
}