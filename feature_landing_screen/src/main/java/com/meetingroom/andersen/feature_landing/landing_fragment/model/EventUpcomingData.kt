package com.meetingroom.andersen.feature_landing.landing_fragment.model

data class EventUpcomingData(
    val title: String,
    val startTime: String,
    val endTime: String,
    val eventDate: String,
    val eventRoom: String,
    val eventRoomColour: Int,
    val reminderRemainingTime: String = "30 min",
    var reminderActive: Boolean = false,
)
