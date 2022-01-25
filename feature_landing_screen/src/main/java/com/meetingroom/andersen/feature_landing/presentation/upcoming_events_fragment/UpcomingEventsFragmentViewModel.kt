package com.meetingroom.andersen.feature_landing.presentation.upcoming_events_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.example.core_module.state.State
import com.example.core_network.RequestResult
import com.meetingroom.andersen.feature_landing.domain.entity.IRoomsEventRepository
import com.meetingroom.andersen.feature_landing.domain.entity.UpcomingEventData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpcomingEventsFragmentViewModel @Inject constructor(
    private val roomsEventRepository: IRoomsEventRepository,
    private var userDataPrefHelper: UserDataPrefHelper
) : ViewModel() {

    private val _upcomingEvents = MutableStateFlow<List<UpcomingEventData>>(emptyList())
    val upcomingEvents: StateFlow<List<UpcomingEventData>> get() = _upcomingEvents.asStateFlow()

    private val _mutableLoadingState = MutableStateFlow<State>(State.Loading)
    val mutableState: StateFlow<State> get() = _mutableLoadingState.asStateFlow()

    fun getUpcomingEvents() {
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                val response = roomsEventRepository.getUpcomingEventData()
                when(response){
                    is RequestResult.Success -> {
                        val listUpcomingEvents = response.data
                        val notDeletedUpcomingEvents = mutableListOf<UpcomingEventData>()
                        listUpcomingEvents.forEach{
                            if(it.status != DELETED){
                                notDeletedUpcomingEvents.add(it)
                                if(checkReminder(it.id)){
                                    getReminder(it)
                                }
                            }
                        }
                        _upcomingEvents.emit(notDeletedUpcomingEvents)
                    }
                    is RequestResult.Error -> {
                        _upcomingEvents.emit(emptyList())
                        _mutableLoadingState.emit(State.Error)
                    }
                    else -> _upcomingEvents.emit(emptyList())
                }
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
            }
        }
    }

    private fun checkReminder(eventId: Long): Boolean{
        val list = userDataPrefHelper.getEventIdsForReminder()
        list?.forEach {
            if (it.toLong() == eventId){
                return true
            }
        }
        return false
    }

    private fun getReminder(event: UpcomingEventData) {
        val time = userDataPrefHelper.getTimeForReminder(event.id).toString()
        event.reminderActive = true
        event.reminderRemainingTime = time
    }

    companion object {
        const val DELETED = "DELETED"
    }
}