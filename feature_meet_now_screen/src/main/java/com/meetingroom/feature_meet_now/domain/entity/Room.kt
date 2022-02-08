package com.meetingroom.feature_meet_now.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Room(
    val id: Long?,
    val title: String?,
    val office: String?,
    val floor: Int?,
    val capacity: Int?,
    val board: Boolean?,
    val projector: Boolean?,
    val color: String?,
    val availableIn: Int?,
    var timeUntilNextEvent: Int?,
    val currentEventEndTime: String?,
    val nextEventStartTime: String?
) : Parcelable