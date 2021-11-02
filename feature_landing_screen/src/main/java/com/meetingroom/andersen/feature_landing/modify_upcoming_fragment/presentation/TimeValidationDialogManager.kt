package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation

import android.annotation.SuppressLint
import com.meetingroom.andersen.feature_landing.R
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@SuppressLint("NewApi")
class TimeValidationDialogManager @Inject constructor() {

    private val _state : MutableStateFlow<ValidationState> = MutableStateFlow(ValidationState.TimeIsValid)
    val state = _state.asStateFlow()

    private val _effect : MutableStateFlow<ValidationEffect> = MutableStateFlow(ValidationEffect.NoEffect)
    val effect = _effect.asStateFlow()

    suspend fun handleEvent(event: ValidationEvent) {
        _effect.emit(ValidationEffect.NoEffect)
        _effect.emit(when (event) {
            is ValidationEvent.OnStartTimeChanged -> validateStartTime(event.startTime, event.endTime, event.date)
            is ValidationEvent.OnEndTimeChanged -> validateEndTime(event.startTime, event.endTime)
            is ValidationEvent.OnDateChanged -> {
                if (isStartTimeBeforeCurrent(event.startTime, event.date)) {
                    _state.value = ValidationState.InvalidStartTime
                    ValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_start_before_current_time_message)
                } else {
                    _state.value = ValidationState.TimeIsValid
                    ValidationEffect.TimeIsValidEffect
                }
            }
        })
    }

    private fun isStartTimeBeforeCurrent(startTime: LocalTime, date: LocalDate): Boolean {
        return startTime.isBefore(LocalTime.now()) && date == LocalDate.now()
    }

    private fun isTimeNotInOfficeHours(startTime: LocalTime, endTime: LocalTime): Boolean {
        return ((startTime.isBefore(MIN_TIME) || startTime.isAfter(MAX_TIME))
                && (endTime.isBefore(MIN_TIME) || endTime.isAfter(MAX_TIME)))
    }

    private fun validateBothTimes(startTime: LocalTime, endTime: LocalTime): ValidationEffect {
        return when {
            endTime.isBefore(startTime) -> {
                _state.value = ValidationState.InvalidEndTime
                ValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_end_before_it_starts_message)
            }
            endTime.minusHours(startTime.hour.toLong()).minusMinutes(startTime.minute.toLong()).isAfter(LocalTime.of(
                MAX_HOURS_DIFF, 0)) -> {
                _state.value = ValidationState.InvalidBothTime
                ValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_last_longer_than_4_hours_message)
            }
            endTime.isBefore(startTime.plusMinutes(MIN_MINUTES_DIFF)) -> {
                _state.value = ValidationState.InvalidBothTime
                ValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_last_less_than_15_minutes_message)
            }
            else -> {
                _state.value = ValidationState.TimeIsValid
                ValidationEffect.TimeIsValidEffect
            }
        }
    }

    private fun validateStartTime(startTime: LocalTime, endTime: LocalTime, date: LocalDate): ValidationEffect {
        return when {
            isStartTimeBeforeCurrent(startTime, date) -> {
                _state.value = ValidationState.InvalidStartTime
                ValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_start_before_current_time_message)
            }
            isTimeNotInOfficeHours(startTime, endTime) -> {
                _state.value = ValidationState.InvalidBothTime
                ValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_start_and_end_between_0_and_6_hours_message)
            }
            startTime.isBefore(MIN_TIME) || startTime.isAfter(MAX_TIME) -> {
                _state.value = ValidationState.InvalidStartTime
                ValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_start_between_0_and_6_hours_message)
            }
            else -> validateBothTimes(startTime, endTime)
        }
    }

    private fun validateEndTime(startTime: LocalTime, endTime: LocalTime): ValidationEffect {
        return when {
            isTimeNotInOfficeHours(startTime, endTime) -> {
                _state.value = ValidationState.InvalidBothTime
                ValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_start_and_end_between_0_and_6_hours_message)
            }
            endTime.isBefore(MIN_TIME) || endTime.isAfter(MAX_TIME) -> {
                _state.value = ValidationState.InvalidEndTime
                ValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_end_between_0_and_6_hours_message)
            }
            else -> validateBothTimes(startTime, endTime)
        }
    }

    companion object {
        private val MIN_TIME = LocalTime.of(6, 0)
        private val MAX_TIME = LocalTime.of(23, 59)
        private const val MAX_HOURS_DIFF = 4
        private const val MIN_MINUTES_DIFF = 15L
    }

    sealed class ValidationEvent {
        data class OnStartTimeChanged(val startTime: LocalTime, val endTime: LocalTime, val date: LocalDate) : ValidationEvent()
        data class OnEndTimeChanged(val startTime: LocalTime, val endTime: LocalTime): ValidationEvent()
        data class OnDateChanged(val startTime: LocalTime, val date: LocalDate) : ValidationEvent()
    }

    sealed class ValidationState {
        object TimeIsValid: ValidationState()
        object InvalidStartTime : ValidationState()
        object InvalidEndTime : ValidationState()
        object InvalidBothTime : ValidationState()
    }

    sealed class ValidationEffect {
        data class ShowInvalidTimeDialog(val messageId: Int) : ValidationEffect()
        object TimeIsValidEffect: ValidationEffect()
        object NoEffect : ValidationEffect()
    }
}