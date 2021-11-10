package com.andersen.feature_rooms_screen.presentation.dialog_rooms.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andersen.feature_rooms_screen.presentation.dialog_rooms.model.GagRoomData
import com.andersen.feature_rooms_screen.presentation.dialog_rooms.model.RoomPickerData

class RoomsViewModel(
    gagForRooms: GagForRooms,
) : ViewModel() {

    val gagRooms = MutableLiveData<List<GagRoomData>>()

    init {
        gagRooms.value = gagForRooms.generate()
    }

    fun changeSelected(rooms: ArrayList<RoomPickerData>, roomName: String) {
        rooms.map {
            it.isSelected = it.room == roomName
        }
    }
}