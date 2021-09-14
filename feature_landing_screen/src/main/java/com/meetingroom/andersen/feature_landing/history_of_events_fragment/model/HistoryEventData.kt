package com.meetingroom.andersen.feature_landing.history_of_events_fragment.model

data class HistoryEventData(
    val title: String,
    val startTime: String,
    val endTime: String,
    val eventDate: String,
    val eventRoom: String,
    val eventRoomColour: Int,
    val bookerName: String,
    val bookerPosition: String,
    val bookerEmail: String,
    val bookerSkype: String,
    val description: String
)

