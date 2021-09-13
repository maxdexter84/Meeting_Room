package com.meetingroom.andersen.feature_landing.upcoming_events_fragment.model

data class UpcomingEventData(
    val title: String,
    val startTime: String,
    val endTime: String,
    val eventDate: String,
    val eventRoom: String,
    val eventRoomColour: Int,
    val reminderRemainingTime: String = "30 min",
    var reminderActive: Boolean,
)