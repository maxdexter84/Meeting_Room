package com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class UpcomingEventsFragmentViewModelFactory(
    private val gagForUpcomingEvents: GagForUpcomingEvents
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpcomingEventsFragmentViewModel::class.java)) {
            return UpcomingEventsFragmentViewModel(
                gagForUpcomingEvents
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}