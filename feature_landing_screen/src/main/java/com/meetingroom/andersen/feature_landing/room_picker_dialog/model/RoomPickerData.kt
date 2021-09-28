package com.meetingroom.andersen.feature_landing.room_picker_dialog.model

data class RoomPickerData(
    val room: String,
    var isSelected: Boolean,
    var isEnabled: Boolean,
)