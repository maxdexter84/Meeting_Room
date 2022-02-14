package com.example.core_module.event_time_validation

import androidx.lifecycle.LiveData
import java.time.LocalDate
import java.time.LocalTime

interface TimeValidationDialogManagerInterface {

    val state: LiveData<TimeValidationState>
    val effect: LiveData<TimeValidationEffect>

    fun handleEvent(event: TimeValidationEvent)

    fun validateBothTimes(startTime: LocalTime, endTime: LocalTime): TimeValidationEffect

    fun validateStartTime(startTime: LocalTime, endTime: LocalTime, date: LocalDate): TimeValidationEffect

    fun validateEndTime(startTime: LocalTime, endTime: LocalTime, date: LocalDate): TimeValidationEffect

    fun isStartTimeBeforeCurrent(startTime: LocalTime, date: LocalDate): Boolean {
        return startTime.isBefore(LocalTime.now()) && date == LocalDate.now()
    }

    companion object {
        val MIN_TIME: LocalTime = LocalTime.of(6, 0, 1)
        val MAX_TIME: LocalTime = LocalTime.of(23, 59)
        const val MAX_HOURS_DIFF = 4
        const val MIN_MINUTES_DIFF = 15L
    }
}

sealed class TimeValidationEvent {
    sealed class UserTimeValidationEvent: TimeValidationEvent() {
        data class OnStartTimeChanged(val startTime: LocalTime, val endTime: LocalTime, val date: LocalDate) : UserTimeValidationEvent()
        data class OnEndTimeChanged(val startTime: LocalTime, val endTime: LocalTime, val date: LocalDate): UserTimeValidationEvent()
        data class OnDateChanged(val startTime: LocalTime, val date: LocalDate) : UserTimeValidationEvent()
    }

    sealed class AdminTimeValidationEvent: TimeValidationEvent() {
        data class OnStartTimeChanged(val startTime: LocalTime, val endTime: LocalTime, val startDate: LocalDate, val endDate: LocalDate) : AdminTimeValidationEvent()
        data class OnEndTimeChanged(val startTime: LocalTime, val endTime: LocalTime, val startDate: LocalDate, val endDate: LocalDate): AdminTimeValidationEvent()
        data class OnDateChanged(val startTime: LocalTime, val endTime: LocalTime, val startDate: LocalDate, val endDate: LocalDate) : AdminTimeValidationEvent()
    }
}

sealed class TimeValidationState {
    object TimeIsValid : TimeValidationState()
    object InvalidStartTime : TimeValidationState()
    object InvalidEndTime : TimeValidationState()
    object InvalidBothTime : TimeValidationState()
}

sealed class TimeValidationEffect {
    data class ShowInvalidTimeDialog(val messageId: Int) : TimeValidationEffect()
    object TimeIsValidEffect: TimeValidationEffect()
}

sealed class DateValidationState {
    object DateIsValid : DateValidationState()
    object InvalidDate : DateValidationState()
}
