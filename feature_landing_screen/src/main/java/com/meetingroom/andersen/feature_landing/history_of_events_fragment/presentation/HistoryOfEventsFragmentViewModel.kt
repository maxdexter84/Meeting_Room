package com.meetingroom.andersen.feature_landing.history_of_events_fragment.presentation

import androidx.lifecycle.ViewModel
import com.example.core_network.RequestMaker
import javax.inject.Inject

class HistoryOfEventsFragmentViewModel @Inject constructor(
    private val requestMaker: RequestMaker
) : ViewModel() {
}