package com.meetingroom.andersen.feature_landing.history_of_events_fragment.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_network.RequestMaker
import com.meetingroom.andersen.feature_landing.history_of_events_fragment.model.HistoryEventData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject

class HistoryOfEventsFragmentViewModel @Inject constructor(
    private val requestMaker: RequestMaker,
) : ViewModel() {

    val gagData = MutableLiveData<List<HistoryEventData>>()

    init {
        viewModelScope.launch {
            delay(1000)
            gagData.value = GagForHistoryEvents().generateData(9)
//            gagData.value = emptyList()
        }
    }
}