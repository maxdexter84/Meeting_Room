package com.meetingroom.feature_meet_now.domain.entity

data class Room (
    val id: Long,
    val title: String,
    val color: Int,
    val nextEventStartTime: String,
    val nextEventEndTime: String
)