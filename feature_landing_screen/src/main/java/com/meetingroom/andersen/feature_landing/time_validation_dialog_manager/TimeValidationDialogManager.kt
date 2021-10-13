package com.meetingroom.andersen.feature_landing.time_validation_dialog_manager

import android.annotation.SuppressLint
import com.meetingroom.andersen.feature_landing.R
import java.time.LocalDate
import java.time.LocalTime

interface UiState

interface UiEvent

interface UiEffect

@SuppressLint("NewApi")
class TimeValidationDialogManager {

    private var currentState: ValidationState = ValidationState.TimeIsValid

    sealed class Event : UiEvent {
        data class OnStartTimeChanged(val startTime: LocalTime, val endTime: LocalTime, val date: LocalDate) : Event()
        data class OnEndTimeChanged(val startTime: LocalTime, val endTime: LocalTime): Event()
        data class OnDateChanged(val startTime: LocalTime, val date: LocalDate) : Event()
    }

    data class State(val validationState: ValidationState) : UiState

    sealed class ValidationState {
        object TimeIsValid: ValidationState()
        object InvalidStartTime : ValidationState()
        object InvalidEndTime : ValidationState()
        object InvalidBothTime : ValidationState()
    }

    sealed class Effect : UiEffect {
        data class ShowInvalidTimeDialog(val messageId: Int) : Effect()
        object NoEffect : Effect()
    }

    fun getEffect(event: Event): Effect {
        return when (event) {
            is Event.OnStartTimeChanged -> validateStartTime(event.startTime, event.endTime, event.date)
            is Event.OnEndTimeChanged -> validateEndTime(event.startTime, event.endTime)
            is Event.OnDateChanged -> {
                if (isStartTimeBeforeCurrent(event.startTime, event.date)) {
                    currentState = ValidationState.InvalidStartTime
                    Effect.ShowInvalidTimeDialog(R.string.event_cant_start_before_current_time_message)
                } else Effect.NoEffect
            }
        }
    }

    fun getState(): State {
        return State(currentState)
    }

    private fun isStartTimeBeforeCurrent(startTime: LocalTime, date: LocalDate): Boolean {
        return startTime.isBefore(LocalTime.now()) && date == LocalDate.now()
    }

    private fun isTimeNotInOfficeHours(startTime: LocalTime, endTime: LocalTime): Boolean {
        return ((startTime.isBefore(MIN_TIME) || startTime.isAfter(MAX_TIME))
                && (endTime.isBefore(MIN_TIME) || endTime.isAfter(MAX_TIME)))
    }

    private fun validateBothTimes(startTime: LocalTime, endTime: LocalTime): Effect {
        return when {
            endTime.isBefore(startTime) -> {
                currentState = ValidationState.InvalidEndTime
                Effect.ShowInvalidTimeDialog(R.string.event_cant_end_before_it_starts_message)
            }
            endTime.minusHours(startTime.hour.toLong()).minusMinutes(startTime.minute.toLong()).isAfter(LocalTime.of(MAX_HOURS_DIFF, 0)) -> {
                currentState = ValidationState.InvalidBothTime
               Effect.ShowInvalidTimeDialog(R.string.event_cant_last_longer_than_4_hours_message)
            }
            endTime.isBefore(startTime.plusMinutes(MIN_MINUTES_DIFF)) -> {
                currentState = ValidationState.InvalidBothTime
                Effect.ShowInvalidTimeDialog(R.string.event_cant_last_less_than_15_minutes_message)
            }
            else -> {
                currentState = ValidationState.TimeIsValid
                Effect.NoEffect
            }
        }
    }

    private fun validateStartTime(startTime: LocalTime, endTime: LocalTime, date: LocalDate): Effect {
        return when {
            isStartTimeBeforeCurrent(startTime, date) -> {
                currentState = ValidationState.InvalidStartTime
                Effect.ShowInvalidTimeDialog(R.string.event_cant_start_before_current_time_message)
            }
            isTimeNotInOfficeHours(startTime, endTime) -> {
                currentState = ValidationState.InvalidBothTime
                Effect.ShowInvalidTimeDialog(R.string.event_cant_start_and_end_between_0_and_6_hours_message)
            }
            startTime.isBefore(MIN_TIME) || startTime.isAfter(MAX_TIME) -> {
                currentState = ValidationState.InvalidStartTime
                Effect.ShowInvalidTimeDialog(R.string.event_cant_start_between_0_and_6_hours_message)
            }
            else -> validateBothTimes(startTime, endTime)
        }
    }

    private fun validateEndTime(startTime: LocalTime, endTime: LocalTime): Effect {
        return when {
            isTimeNotInOfficeHours(startTime, endTime) -> {
                currentState = ValidationState.InvalidBothTime
                Effect.ShowInvalidTimeDialog(R.string.event_cant_start_and_end_between_0_and_6_hours_message)
            }
            endTime.isBefore(MIN_TIME) || endTime.isAfter(MAX_TIME) -> {
                currentState = ValidationState.InvalidEndTime
                Effect.ShowInvalidTimeDialog(R.string.event_cant_end_between_0_and_6_hours_message)
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
}