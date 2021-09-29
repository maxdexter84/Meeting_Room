package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ModifyUpcomingEventViewModel : ViewModel() {

    val userRoom = MutableLiveData<String?>()

    fun update(room: String) {
        userRoom.value = room
    }
}