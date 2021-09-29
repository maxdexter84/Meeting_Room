package com.meetingroom.andersen.feature_landing.room_picker_dialog.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.GagRoomData
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.RoomAndTimePickerData

class RoomPickerViewModel(
    gagForRooms: GagForRooms,
    private val saveData: UserDataPrefHelperImpl
) : ViewModel() {

    val gagRooms = MutableLiveData<List<GagRoomData>>()
    val userChosenRoom = MutableLiveData<String?>()

    init {
        gagRooms.value = gagForRooms.generate()
        userChosenRoom.value = saveData.getRoomOfUserSelection()
    }

    fun saveUserChosenRoom(roomName: String) {
        saveData.saveRoomOfUserSelection(roomName)
        userChosenRoom.value = saveData.getRoomOfUserSelection()
    }

    fun updateUserChosenRoom(room : String) {
        userChosenRoom.value = room
    }

    fun changeSelected(roomsAndTime: ArrayList<RoomAndTimePickerData>, roomName: String) {
        roomsAndTime.map {
            it.isSelected = it.roomAndTime == roomName
        }
    }
}