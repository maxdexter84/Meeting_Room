package com.andersen.feature_rooms_screen.domain.entity.remote

data class ActivateRoomEventDto(
    val description: String,
    val roomId: Long,
    val id: Long,
    val startDateTime: String,
    val endDateTime: String,
    val title: String
)