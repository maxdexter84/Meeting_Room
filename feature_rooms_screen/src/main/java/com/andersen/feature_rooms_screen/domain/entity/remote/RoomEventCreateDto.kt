package com.andersen.feature_rooms_screen.domain.entity.remote

data class RoomEventCreateDto(
    val description: String,
    val roomId: Long,
    val startDateTime: String,
    val endDateTime: String,
    val title: String
)