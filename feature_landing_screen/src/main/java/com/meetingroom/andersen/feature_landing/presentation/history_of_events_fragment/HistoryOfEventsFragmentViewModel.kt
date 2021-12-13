package com.meetingroom.andersen.feature_landing.presentation.history_of_events_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_network.RequestMaker
import com.meetingroom.andersen.feature_landing.data.RoomsApi
import com.meetingroom.andersen.feature_landing.domain.entity.HistoryEventData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryOfEventsFragmentViewModel @Inject constructor(
    private val requestMaker: RequestMaker,
    roomsApi: RoomsApi
) : ViewModel() {

    val gagData = MutableLiveData<List<HistoryEventData>>()

    init {
        viewModelScope.launch {
            delay(1000)
            gagData.value = roomsApi.getGagForHistoryEvents()
        }
    }
}