package com.meetingroom.andersen.feature_landing.history_of_events_fragment.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core_network.RequestMaker

@Suppress("UNCHECKED_CAST")
class HistoryOfEventsViewModelFactory(val requestMaker: RequestMaker) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryOfEventsFragmentViewModel::class.java)) {
            return HistoryOfEventsFragmentViewModel(requestMaker) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
