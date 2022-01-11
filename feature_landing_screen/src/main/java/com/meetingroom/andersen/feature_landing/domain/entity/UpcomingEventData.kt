package com.meetingroom.andersen.feature_landing.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpcomingEventData(
    val id: Long,
    var title: String,
    var description: String,
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
    var startTime: String,
    var endTime: String,
    var eventDate: String,
    val color: String,
    var reminderRemainingTime: String,
    var reminderActive: Boolean
) : Parcelable
