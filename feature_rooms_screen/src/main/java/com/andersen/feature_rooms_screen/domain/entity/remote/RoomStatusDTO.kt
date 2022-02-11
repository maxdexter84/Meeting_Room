package com.andersen.feature_rooms_screen.domain.entity.remote

data class RoomStatusDTO(
	val id: Long,
	val title: String,
	val status: String,
	val eventIdsList: List<Long>
)