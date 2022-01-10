package com.meetingroom.feature_meet_now.domain.entity

data class Room (
    val id: Long,
    val title: String,
    val color: String,
    val timeUntilNextEvent: Int?
)