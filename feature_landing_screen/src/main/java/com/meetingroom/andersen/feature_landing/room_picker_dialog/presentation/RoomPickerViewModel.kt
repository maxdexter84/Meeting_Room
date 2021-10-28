package com.meetingroom.andersen.feature_landing.room_picker_dialog.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.GagRoomData
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.RoomPickerData

class RoomPickerViewModel(
    gagForRooms: GagForRooms,
) : ViewModel() {

    val gagRooms = MutableLiveData<List<GagRoomData>>()

    init {
        gagRooms.value = gagForRooms.generate()
    }

    fun changeSelected(roomsAndTime: ArrayList<RoomPickerData>, roomName: String) {
        roomsAndTime.map {
            it.isSelected = it.room == roomName
        }
    }
}