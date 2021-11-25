package com.andersen.feature_rooms_screen.domain.entity

data class RoomPickerNewEventData(
    val room: String,
    var isSelected: Boolean,
    var isEnabled: Boolean,
)
