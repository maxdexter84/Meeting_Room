package com.meetingroom.andersen.feature_landing.presentation.upcoming_events_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetingroom.andersen.feature_landing.data.RoomsApi
import com.meetingroom.andersen.feature_landing.domain.entity.UpcomingEventData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpcomingEventsFragmentViewModel @Inject constructor(
    roomsApi: RoomsApi,
) : ViewModel() {

    val gagData = MutableLiveData<List<UpcomingEventData>>()

    init {
        viewModelScope.launch {
            delay(1000)
            gagData.value = roomsApi.getUpcomingEventData()
        }
    }
}