package com.meetingroom.andersen.feature_landing.room_picker_dialog.model

data class RoomAndTimePickerData(
    val roomAndTime: String,
    var isSelected: Boolean,
    var isEnabled: Boolean,
)