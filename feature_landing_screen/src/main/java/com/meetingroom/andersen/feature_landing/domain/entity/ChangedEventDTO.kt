package com.meetingroom.andersen.feature_landing.domain.entity

data class ChangedEventDTO(
    var description: String,
    var endDateTime: String,
    val id: Long,
    val roomId: Long,
    var startDateTime: String,
    var title: String
)
