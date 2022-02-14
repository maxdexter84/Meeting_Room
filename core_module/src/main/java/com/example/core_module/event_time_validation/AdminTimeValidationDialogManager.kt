package com.example.core_module.event_time_validation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.core_module.R
import com.example.core_module.event_time_validation.TimeValidationDialogManagerInterface.Companion.MIN_MINUTES_DIFF
import com.example.core_module.utils.DateTimePickerConstants.MINUTE_TO_ROUND
import com.example.core_module.utils.roundUpMinute
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class AdminTimeValidationDialogManager @Inject constructor(): TimeValidationDialogManagerInterface {

    private val _state = MutableLiveData<TimeValidationState>()
    override val state: LiveData<TimeValidationState> = _state

    private val _effect = MutableLiveData<TimeValidationEffect>()
    override val effect: LiveData<TimeValidationEffect> = _effect

    private val _dateState = MutableLiveData<DateValidationState>()
    val dateState: LiveData<DateValidationState> = _dateState

    override fun handleEvent(event: TimeValidationEvent) {
        if (event is TimeValidationEvent.AdminTimeValidationEvent) {
            handleAdminEvent(event)
        }
    }

    private fun handleAdminEvent(event: TimeValidationEvent.AdminTimeValidationEvent) {
        _effect.value = when (event) {
            is TimeValidationEvent.AdminTimeValidationEvent.OnStartTimeChanged -> {
                if (event.startDate.isEqual(LocalDate.now()) || event.startDate.isEqual(event.endDate)) {
                    validateStartTimeCurrentDate(event.startTime)
                } else {
                    _state.value = TimeValidationState.TimeIsValid
                    TimeValidationEffect.TimeIsValidEffect
                }
            }
            is TimeValidationEvent.AdminTimeValidationEvent.OnEndTimeChanged -> {
                if (event.startDate.isEqual(event.endDate)) {
                    validateEndTime(
                        event.startTime,
                        event.endTime,
                        event.endDate
                    )
                } else {
                    _state.value = TimeValidationState.TimeIsValid
                    TimeValidationEffect.TimeIsValidEffect
                }
            }
            is TimeValidationEvent.AdminTimeValidationEvent.OnDateChanged -> {
                val startTime = event.startTime.roundUpMinute(MINUTE_TO_ROUND)
                val endTime = event.endTime.roundUpMinute(MINUTE_TO_ROUND)
                when (validateDate(event.startDate, event.endDate)) {
                    DateValidationEffect.DateIsInvalidEffect -> {
                        _dateState.value = DateValidationState.InvalidDate
                        _state.value = TimeValidationState.InvalidBothTime
                        TimeValidationEffect.ShowInvalidTimeDialog(
                            R.string.event_cant_end_before_it_starts_message
                        )
                    }
                    DateValidationEffect.DateIsValidEffect -> {
                        _dateState.value = DateValidationState.DateIsValid
                        when {
                            event.startDate.isEqual(event.endDate) -> {
                                validateStartTime(
                                    startTime,
                                    endTime,
                                    event.startDate
                                )
                            }
                            event.startDate.isEqual(LocalDate.now()) -> {
                                validateStartTimeCurrentDate(startTime)
                            }
                            else -> {
                                _state.value = TimeValidationState.TimeIsValid
                                TimeValidationEffect.TimeIsValidEffect
                            }
                        }
                    }
                }
            }
        }
    }

    private fun validateStartTimeCurrentDate(startTime: LocalTime) =
        if (startTime.isBefore(LocalTime.now())) {
            TimeValidationEffect.ShowInvalidTimeDialog(
                R.string.event_cant_start_before_current_time_message
            )

        } else {
            _state.value = TimeValidationState.TimeIsValid
            TimeValidationEffect.TimeIsValidEffect
        }

    override fun validateBothTimes(startTime: LocalTime, endTime: LocalTime): TimeValidationEffect {
        return when {
            endTime.isBefore(startTime) -> {
                _state.value = TimeValidationState.InvalidEndTime
                TimeValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_end_before_it_starts_message)
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

    override fun validateStartTime(
        startTime: LocalTime,
        endTime: LocalTime,
        date: LocalDate
    ): TimeValidationEffect {
        return when {
            isStartTimeBeforeCurrent(startTime, date) -> {
                _state.value = TimeValidationState.InvalidStartTime
                TimeValidationEffect.ShowInvalidTimeDialog(R.string.event_cant_start_before_current_time_message)
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

     override fun validateEndTime(
        startTime: LocalTime,
        endTime: LocalTime,
        date: LocalDate
    ): TimeValidationEffect =
        if (_state.value != TimeValidationState.InvalidStartTime &&
            _state.value != TimeValidationState.InvalidBothTime
        ) {
            validateBothTimes(startTime, endTime)
        } else {
            validateStartTime(startTime, endTime, date)
        }

    private fun validateDate(startDate: LocalDate, endDate: LocalDate): DateValidationEffect {
        return if (startDate.isEqual(endDate) || startDate.isBefore(endDate)) {
            DateValidationEffect.DateIsValidEffect
        } else DateValidationEffect.DateIsInvalidEffect
    }
}

sealed class DateValidationEffect {
    object DateIsValidEffect: DateValidationEffect()
    object DateIsInvalidEffect: DateValidationEffect()
}
