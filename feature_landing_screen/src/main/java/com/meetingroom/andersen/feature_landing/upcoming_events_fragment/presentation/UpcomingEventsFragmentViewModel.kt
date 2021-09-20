package com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.model.UpcomingEventData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UpcomingEventsFragmentViewModel(
    gagForUpcomingEvents: GagForUpcomingEvents
) : ViewModel() {

    val gagData = MutableLiveData<List<UpcomingEventData>>()

    init {
        viewModelScope.launch {
            delay(1000)
            gagData.value = gagForUpcomingEvents.generate(9)
        }
    }
}