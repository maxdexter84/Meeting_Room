package com.andersen.feature_rooms_screen.presentation.dialog_rooms.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.presentation.dialog_rooms.model.RoomPickerData
import javax.inject.Inject

class RoomsViewModel @Inject constructor(
    gagForRooms: GagForRooms
) : ViewModel() {

    val gagRooms = MutableLiveData<List<Room>>()

    init {
        gagRooms.value = gagForRooms.generate()
    }

    fun changeSelected(rooms: ArrayList<RoomPickerData>, title: String) {
        rooms.map {
            it.isSelected = it.room == title
        }
    }
}