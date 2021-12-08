package com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.state.State
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.model.UpcomingEventData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UpcomingEventsFragmentViewModel(
    private val gagForUpcomingEvents: GagForUpcomingEvents
) : ViewModel() {

    private val _upcomingEvents = MutableStateFlow<List<UpcomingEventData>>(emptyList())
    val upcomingEvents: StateFlow<List<UpcomingEventData>> get() = _upcomingEvents.asStateFlow()

    private val _mutableLoadingState = MutableStateFlow<State>(State.Loading)
    val mutableState: StateFlow<State> get() = _mutableLoadingState.asStateFlow()

    init {
        getUpcomingEvents()
    }

    private fun getUpcomingEvents(){
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                delay(1000)
                _upcomingEvents.emit(gagForUpcomingEvents.generate(9))
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
            }
        }
    }
}