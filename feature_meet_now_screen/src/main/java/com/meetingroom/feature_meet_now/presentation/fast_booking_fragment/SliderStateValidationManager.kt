package com.meetingroom.feature_meet_now.presentation.fast_booking_fragment

private const val MIN_EVENT_TIME_LIMIT = 15
private const val MAX_EVENT_TIME_LIMIT = 90
private const val EVENT_START_TIME_LIMIT = 30

class SliderStateValidationManager {

    fun validate(sliderState: RangeSliderState): ValidationState {
        with(sliderState) {
            if (startSelected > EVENT_START_TIME_LIMIT) {
                return ValidationState.Invalid.START_TIME_VIOLATION
            }
            if (endSelected - startSelected < MIN_EVENT_TIME_LIMIT) {
                return ValidationState.Invalid.MIN_EVENT_TIME_VIOLATION
            }
            if (endSelected - startSelected > MAX_EVENT_TIME_LIMIT) {
                return ValidationState.Invalid.MAX_EVENT_TIME_VIOLATION
            }
            return ValidationState.Valid
        }
    }
}

sealed interface ValidationState {
    object Valid : ValidationState
    enum class Invalid : ValidationState {
        START_TIME_VIOLATION,
        MIN_EVENT_TIME_VIOLATION,
        MAX_EVENT_TIME_VIOLATION
    }
}
