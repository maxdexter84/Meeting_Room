package com.andersen.feature_rooms_screen.rooms_event_grid_fragment.model

data class Room (
    val capacity: Int,
    val floor: Int,
    val id: Long,
    val office: String,
    val title: String,
    val color: Int
)