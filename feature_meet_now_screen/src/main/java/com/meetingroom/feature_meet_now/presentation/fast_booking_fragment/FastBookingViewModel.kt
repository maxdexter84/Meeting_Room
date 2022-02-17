package com.meetingroom.feature_meet_now.presentation.fast_booking_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andersen.feature_rooms_screen.domain.entity.remote.ActivateRoomEventDto
import com.andersen.feature_rooms_screen.domain.entity.remote.RoomEventCreateDto
import com.andersen.feature_rooms_screen.presentation.RoomsEventViewModel
import com.example.core_module.utils.TimeUtilsConstants.TIME_DATE_FORMAT
import com.example.core_module.utils.TimeUtilsConstants.TIME_FORMAT
import com.example.core_module.utils.stringTimeToStringDateTime
import com.example.core_network.RequestResult
import com.meetingroom.feature_meet_now.data.AvailableRoomsRepository
import com.meetingroom.feature_meet_now.domain.entity.Room
import com.meetingroom.feature_meet_now.presentation.fast_booking_fragment.Constants.MAX_EVENT_TIME
import com.meetingroom.feature_meet_now.presentation.fast_booking_fragment.Constants.MIN_EVENT_TIME
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val SLIDER_START_THUMB_INDEX = 0
private const val SLIDER_END_THUMB_INDEX = 1
private const val EMPTY_STRING = ""

class FastBookingViewModel @AssistedInject constructor(
    @Assisted val room: Room,
    val availableRoomsRepository: AvailableRoomsRepository
) : ViewModel() {

    private val rangeSliderAdapter = RangeSliderAdapter(room)

    private val sliderState = rangeSliderAdapter.sliderState

    private val validationManager = SliderStateValidationManager()

    private val _sliderStartFlow = MutableStateFlow(sliderState.startSelected)
    val sliderStartFlow: StateFlow<Int> get() = _sliderStartFlow.asStateFlow()

    private val _sliderEndFlow = MutableStateFlow(sliderState.endSelected)
    val sliderEndFlow: StateFlow<Int> get() = _sliderEndFlow.asStateFlow()

    private val _validationStateFlow = MutableStateFlow<ValidationState>(ValidationState.Valid)
    val validationStateFlow: StateFlow<ValidationState> get() = _validationStateFlow.asStateFlow()

    private val _eventCreatedFlow = MutableStateFlow<Boolean>(false)
    val eventCreatedFlow: StateFlow<Boolean> get() = _eventCreatedFlow

    private val _startLimitTimeStringData = MutableLiveData<String>()
    val startLimitTimeStringData: LiveData<String> get() = _startLimitTimeStringData

    private val _endLimitTimeStringData = MutableLiveData<String>()
    val endLimitTimeStringData: LiveData<String> get() = _endLimitTimeStringData

    private val _sliderStateData = MutableLiveData<RangeSliderState>()
    val sliderStateData: LiveData<RangeSliderState> get() = _sliderStateData

    init {
        _startLimitTimeStringData.value = rangeSliderAdapter.startLimitTimeString
        _endLimitTimeStringData.value = rangeSliderAdapter.endLimitTimeString
        _sliderStateData.value = sliderState
    }

    fun bookRoom() {
        viewModelScope.launch {
            createEvent()
        }
    }

    private suspend fun createEvent() {
        if (room.id != null) {
            when (val res = availableRoomsRepository.createEvent(
                RoomEventCreateDto(
                    roomId = room.id,
                    title = EMPTY_STRING,
                    description = EMPTY_STRING,
                    startDateTime = rangeSliderAdapter.getSelectedTime(sliderState.startSelected)
                        .stringTimeToStringDateTime(TIME_FORMAT, TIME_DATE_FORMAT),
                    endDateTime = rangeSliderAdapter.getSelectedTime(sliderState.endSelected)
                        .stringTimeToStringDateTime(TIME_FORMAT, TIME_DATE_FORMAT)
                )
            )
            ) {
                is RequestResult.Success -> {
                    activateEvent(res.data.id)
                }
                is RequestResult.Error -> {
                }
                else -> Unit
            }
        }
    }

    private suspend fun activateEvent(eventId: Long) {
        if (room.id != null) {
            when (val res = availableRoomsRepository.activateEvent(
                ActivateRoomEventDto(
                    id = eventId,
                    roomId = room.id,
                    title = EMPTY_STRING,
                    description = EMPTY_STRING,
                    startDateTime = rangeSliderAdapter.getSelectedTime(sliderState.startSelected)
                        .stringTimeToStringDateTime(TIME_FORMAT, TIME_DATE_FORMAT),
                    endDateTime = rangeSliderAdapter.getSelectedTime(sliderState.endSelected)
                        .stringTimeToStringDateTime(TIME_FORMAT, TIME_DATE_FORMAT)
                )
            )) {
                is RequestResult.Error -> {
                    _eventCreatedFlow.emit(res.code == RoomsEventViewModel.NULL_POINTER_EXCEPTION_CODE)
                }
                else -> Unit
            }
        }
    }

    fun getSelectedTime(sliderPosition: Int): String {
        return rangeSliderAdapter.getSelectedTime(sliderPosition)
    }

    fun getSliderStateSelectedValues(): List<Float> {
        return listOf(sliderState.startSelected.toFloat(), sliderState.endSelected.toFloat())
    }

    fun getSliderStateStartLimit(): Int {
        return sliderState.startLimit
    }

    fun getSliderStateEndLimit(): Int {
        return sliderState.endLimit
    }

    fun onSliderStateChanged(thumbIndex: Int, values: List<Float>) {
        var validationState: ValidationState = ValidationState.Valid
        with(sliderState) {
            if (thumbIndex == SLIDER_START_THUMB_INDEX) {
                startSelected = values[SLIDER_START_THUMB_INDEX].toInt()
                validationState = validationManager.validate(this)
                if (validationState is ValidationState.Invalid) {
                    when (validationState) {
                        ValidationState.Invalid.START_TIME_VIOLATION ->
                            startSelected = rangeSliderAdapter.getLastValidStart()
                        ValidationState.Invalid.MIN_EVENT_TIME_VIOLATION ->
                            startSelected = endSelected - MIN_EVENT_TIME
                        ValidationState.Invalid.MAX_EVENT_TIME_VIOLATION ->
                            startSelected = endSelected - MAX_EVENT_TIME
                    }
                }
            }
            if (thumbIndex == SLIDER_END_THUMB_INDEX) {
                endSelected = values[SLIDER_END_THUMB_INDEX].toInt()
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
