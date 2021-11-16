package com.andersen.feature_rooms_screen.presentation.dialog_rooms.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andersen.feature_rooms_screen.data.RoomsApi
import com.andersen.feature_rooms_screen.domain.entity.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomsViewModel @Inject constructor(
    private val roomsApi: RoomsApi
) : ViewModel() {

    private val _mutableRoomList = MutableStateFlow<List<Room>>(emptyList())
    val mutableRoomList: StateFlow<List<Room>> get() = _mutableRoomList.asStateFlow()

    init {
        getRoomsList()
    }

    private fun getRoomsList() {
        viewModelScope.launch {
            try {
                _mutableRoomList.emit(roomsApi.getGagRoomsForDialog())
            } catch (exception: Exception) {

            }
        }
    }
}