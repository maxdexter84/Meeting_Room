package com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.model.UpcomingEventData

class UpcomingEventsFragmentViewModel(
    gagForUpcomingEvents: GagForUpcomingEvents
) : ViewModel() {

    val gagData = MutableLiveData<List<UpcomingEventData>>()

    init {
        gagData.value = gagForUpcomingEvents.generate(9)
    }
}