package com.meetingroom.andersen.feature_landing.presentation.history_of_events_fragment

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_network.RequestResult
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.domain.entity.HistoryEventData
import com.meetingroom.andersen.feature_landing.domain.entity.HistoryEventDataDTO
import com.meetingroom.andersen.feature_landing.domain.entity.IRoomsEventRepository
import com.meetingroom.andersen.feature_landing.domain.entity.SkypeInitData
import com.meetingroom.andersen.feature_landing.domain.mappers.toHistoryEventsList
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryOfEventsFragmentViewModel @Inject constructor(
    private val roomsEventRepository: IRoomsEventRepository,
    val appContext: Context
) : ViewModel() {

    private val roomsEventRequestResult: MutableLiveData<RequestResult<List<HistoryEventDataDTO>>> by lazy {
        MutableLiveData()
    }

    private val historyEventsLiveData = MutableLiveData<List<HistoryEventData>>()
    val historyEvents: LiveData<List<HistoryEventData>>
        get() = historyEventsLiveData

    private val _skypeInstallData : MutableLiveData<SkypeInitData> by lazy {
        MutableLiveData()
    }
    val isSkypeInstallData: LiveData<SkypeInitData>
        get() = _skypeInstallData

    private val _skypeNotInstall: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }
    val skypeNotInstall: LiveData<Boolean>
        get() = _skypeNotInstall

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

    private fun checkResponse(response: RequestResult<List<HistoryEventDataDTO>>) {
        when (response) {
            is RequestResult.Success -> {
                val listHistoryEventsDTO = response.data
                val notDeletedHistoryEvents = mutableListOf<HistoryEventData>()
                listHistoryEventsDTO.toHistoryEventsList().forEach{
                    if(it.status != DELETED){
                        notDeletedHistoryEvents.add(it)
                    }
                }
                historyEventsLiveData.postValue(notDeletedHistoryEvents)
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

    fun openSkypeDialog(nickname: String) {
        if (!isSkypeInstalled()) {
            _skypeNotInstall.value = false
        } else {
            _skypeInstallData.postValue(SkypeInitData(true, nickname))
        }
    }
    private fun isSkypeInstalled(): Boolean {
        val packageManager: PackageManager = appContext.packageManager
        try {
            packageManager.getPackageInfo(
                appContext.getString(R.string.skype_package_name),
                PackageManager.GET_ACTIVITIES
            )
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
        return true
    }

    companion object {
        const val DELETED = "DELETED"
    }
}