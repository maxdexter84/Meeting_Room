package com.meetingroom.andersen.feature_landing.room_picker_dialog.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.GagRoomData

class RoomPickerViewModel(
    gagForRooms: GagForRooms,
    private val saveData: UserDataPrefHelperImpl
) : ViewModel() {

    val gagRooms = MutableLiveData<List<GagRoomData>>()

    init {
        gagRooms.value = gagForRooms.generate()
    }

    fun getUserChosenRoom(): String? = saveData.getRoomOfUserSelection()

    fun saveUserChosenRoom(roomName: String) {
        saveData.saveRoomOfUserSelection(roomName)
    }
}