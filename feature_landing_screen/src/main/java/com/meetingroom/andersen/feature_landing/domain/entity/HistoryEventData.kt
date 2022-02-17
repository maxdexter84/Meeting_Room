package com.meetingroom.andersen.feature_landing.domain.entity

data class HistoryEventData(
    val id: Long,
    val title: String,
    val description: String,
    val startDateTime: String,
    val endDateTime: String,
    val roomId: Long,
    val room: String,
    val userFullName: String,
    val userId: Long,
    val userPosition: String,
    val userEmail: String,
    val userSkype: String,
    val status: String,
    val color: String,
    var startTime: String,
    var endTime: String,
    var eventDate: String,
    var isOpened: Boolean
)

