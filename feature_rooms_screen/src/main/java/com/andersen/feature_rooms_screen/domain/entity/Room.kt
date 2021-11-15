package com.andersen.feature_rooms_screen.domain.entity

data class Room (
    val capacity: Int,
    val floor: Int,
    val id: Long,
    val office: String,
    val title: String,
    val color: Int
)