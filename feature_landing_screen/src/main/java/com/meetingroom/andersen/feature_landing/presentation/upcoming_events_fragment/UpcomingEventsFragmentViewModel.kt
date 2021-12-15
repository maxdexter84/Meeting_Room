package com.meetingroom.andersen.feature_landing.presentation.upcoming_events_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.state.State
import com.meetingroom.andersen.feature_landing.data.RoomsApi
import com.meetingroom.andersen.feature_landing.domain.entity.UpcomingEventData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpcomingEventsFragmentViewModel @Inject constructor(
    private val roomsApi: RoomsApi
) : ViewModel() {

    private val _upcomingEvents = MutableStateFlow<List<UpcomingEventData>>(emptyList())
    val upcomingEvents: StateFlow<List<UpcomingEventData>> get() = _upcomingEvents.asStateFlow()

    private val _mutableLoadingState = MutableStateFlow<State>(State.Loading)
    val mutableState: StateFlow<State> get() = _mutableLoadingState.asStateFlow()

    init {
        getUpcomingEvents()
    }

    private fun getUpcomingEvents() {
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                _upcomingEvents.emit(roomsApi.getUpcomingEventData())
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
            }
        }
    }
}