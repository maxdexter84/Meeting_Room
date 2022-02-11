package com.andersen.feature_rooms_screen.domain.entity.remote

data class RoomEventResponseDto(
    val id: Long,
    val roomId: Long,
    val startDateTime: String,
    val endDateTime: String
)