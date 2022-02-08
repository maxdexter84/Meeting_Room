package com.meetingroom.feature_meet_now.presentation.fast_booking_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetingroom.feature_meet_now.domain.entity.Room
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val MIN_START_TIME = 30F
private const val MIN_EVENT_TIME = 15F
private const val MIN_TIME_RANGE = 0F
private const val MAX_TIME_RANGE = 120F
private const val MAX_EVENT_TIME = 90F

private const val SLIDER_START_THUMB_INDEX = 0
private const val SLIDER_END_THUMB_INDEX = 1

class FastBookingViewModel @AssistedInject constructor(
    @Assisted val room: Room
) : ViewModel() {

    val sliderState = RangeSliderState(
        startLimit = MIN_TIME_RANGE,
        endLimit = MAX_TIME_RANGE,
        startSelected = MIN_TIME_RANGE,
        endSelected = MAX_EVENT_TIME
    )
    var rangeSliderAdapter = RangeSliderAdapter(room)
    private val validationManager = SliderStateValidationManager()

    private val _sliderStartFlow = MutableStateFlow(sliderState.startSelected)
    val sliderStartFlow: StateFlow<Float> get() = _sliderStartFlow.asStateFlow()

    private val _sliderEndFlow = MutableStateFlow(sliderState.endSelected)
    val sliderEndFlow: StateFlow<Float> get() = _sliderEndFlow.asStateFlow()

    private val _validationStateFlow = MutableStateFlow<ValidationState>(ValidationState.Valid)
    val validationStateFlow: StateFlow<ValidationState> get() = _validationStateFlow.asStateFlow()

    fun onSliderStateChanged(thumbIndex: Int, values: List<Float>) {
        var validationState: ValidationState = ValidationState.Valid
        with(sliderState) {
            if (thumbIndex == SLIDER_START_THUMB_INDEX) {
                startSelected = values[SLIDER_START_THUMB_INDEX]
                validationState = validationManager.validate(this)
                if (validationState is ValidationState.Invalid) {
                    when (validationState) {
                        ValidationState.Invalid.START_TIME_VIOLATION ->
                            startSelected = MIN_START_TIME
                        ValidationState.Invalid.MIN_EVENT_TIME_VIOLATION ->
                            startSelected = endSelected - MIN_EVENT_TIME
                        ValidationState.Invalid.MAX_EVENT_TIME_VIOLATION ->
                            startSelected = endSelected - MAX_EVENT_TIME
                    }
                }
            }
            if (thumbIndex == SLIDER_END_THUMB_INDEX) {
                endSelected = values[SLIDER_END_THUMB_INDEX]
                validationState = validationManager.validate(this)
                if (validationState is ValidationState.Invalid) {
                    when (validationState) {
                        ValidationState.Invalid.MIN_EVENT_TIME_VIOLATION ->
                            endSelected = startSelected + MIN_EVENT_TIME
                        ValidationState.Invalid.MAX_EVENT_TIME_VIOLATION ->
                            endSelected = startSelected + MAX_EVENT_TIME
                    }
                }
            }
            viewModelScope.launch {
                _validationStateFlow.emit(validationState)
                _sliderStartFlow.emit(startSelected)
                _sliderEndFlow.emit(endSelected)
            }
            _validationStateFlow.value = ValidationState.Valid
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(room: Room): FastBookingViewModel
    }
}