package com.meetingroom.andersen.feature_landing.upcoming_events_fragment.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpcomingEventData(
    var title: String,
    var startTime: String,
    var endTime: String,
    var eventDate: String,
    var eventRoom: String,
    val eventRoomColour: Int,
    var reminderRemainingTime: String = "30 minutes before",
    var reminderActive: Boolean,
    var eventDescription: String?,
) : Parcelable
