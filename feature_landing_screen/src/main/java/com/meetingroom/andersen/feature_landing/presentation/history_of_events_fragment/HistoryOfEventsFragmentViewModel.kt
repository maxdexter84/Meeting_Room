package com.meetingroom.andersen.feature_landing.presentation.history_of_events_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_network.RequestResult
import com.meetingroom.andersen.feature_landing.domain.entity.HistoryEventData
import com.meetingroom.andersen.feature_landing.domain.entity.IRoomsEventRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryOfEventsFragmentViewModel @Inject constructor(
    private val roomsEventRepository: IRoomsEventRepository
) : ViewModel() {

    private val roomsEventRequestResult: MutableLiveData<RequestResult<List<HistoryEventData>>> by lazy {
        MutableLiveData()
    }

    private val historyEventsLiveData = MutableLiveData<List<HistoryEventData>>()
    val historyEvents: LiveData<List<HistoryEventData>>
        get() = historyEventsLiveData

    init {
        getHistoryEvents()
    }

    private fun getHistoryEvents() {
        roomsEventRequestResult.postValue(RequestResult.Loading)
        viewModelScope.launch {
            val response = roomsEventRepository.getHistoryEvents()
            checkResponse(response)
        }
    }

    private fun checkResponse(response: RequestResult<List<HistoryEventData>>) {
        when (response) {
            is RequestResult.Success -> {
                historyEventsLiveData.postValue(response.data)
            }
            is RequestResult.Error -> {
                historyEventsLiveData.postValue(emptyList())
                roomsEventRequestResult.postValue(
                    RequestResult.Error(
                        response.exception,
                        response.code
                    )
                )
            }
            else -> historyEventsLiveData.postValue(emptyList())
        }
        roomsEventRequestResult.value = response
    }
}