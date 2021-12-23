package com.meetingroom.feature_meet_now.presentation.available_now_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetingroom.feature_meet_now.domain.entity.Room
import com.meetingroom.feature_meet_now.data.RoomsApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomsAvailableNowViewModel @Inject constructor(
    private val roomsApi: RoomsApi
) : ViewModel() {
    private val _roomsAvailableNow = MutableStateFlow<List<Room>>(emptyList())
    val roomsAvailableNow: StateFlow<List<Room>> get() = _roomsAvailableNow.asStateFlow()

    init {
        getRoomsAvailableNow()
    }

    fun getRoomsAvailableNow() {
        viewModelScope.launch {
            _roomsAvailableNow.emit(roomsApi.getRoomsAvailableNow())
        }
    }
}