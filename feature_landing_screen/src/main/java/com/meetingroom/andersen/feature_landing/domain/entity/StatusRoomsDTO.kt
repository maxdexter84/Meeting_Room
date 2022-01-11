package com.meetingroom.andersen.feature_landing.domain.entity

data class StatusRoomsDTO(
    val id: Long,
    val title: String,
    var isSelected: Boolean,
    var isEnabled: Boolean,
)
