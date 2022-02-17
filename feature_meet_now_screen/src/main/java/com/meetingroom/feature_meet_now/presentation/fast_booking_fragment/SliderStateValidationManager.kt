package com.meetingroom.feature_meet_now.presentation.fast_booking_fragment

import com.meetingroom.feature_meet_now.presentation.fast_booking_fragment.Constants.MAX_EVENT_TIME
import com.meetingroom.feature_meet_now.presentation.fast_booking_fragment.Constants.MIN_EVENT_TIME
import com.meetingroom.feature_meet_now.presentation.fast_booking_fragment.Constants.START_TIME_LIMIT

class SliderStateValidationManager {

    fun validate(sliderState: RangeSliderState): ValidationState {
        with(sliderState) {
            if (startSelected > START_TIME_LIMIT && startSelected > startLimit) {
                return ValidationState.Invalid.START_TIME_VIOLATION
            }
            if (endSelected - startSelected < MIN_EVENT_TIME) {
                return ValidationState.Invalid.MIN_EVENT_TIME_VIOLATION
            }
            if (endSelected - startSelected > MAX_EVENT_TIME) {
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
