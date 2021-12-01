package com.andersen.feature_rooms_screen.domain.entity.rooms_event_grid

data class RoomPickerData(
    val room: String,
    var isSelected: Boolean,
    var isAllRooms: Boolean
)