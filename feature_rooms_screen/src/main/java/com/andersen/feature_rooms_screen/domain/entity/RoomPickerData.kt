package com.andersen.feature_rooms_screen.domain.entity

data class RoomPickerData(
    val room: String,
    var isSelected: Boolean,
    var isAllRooms: Boolean
)