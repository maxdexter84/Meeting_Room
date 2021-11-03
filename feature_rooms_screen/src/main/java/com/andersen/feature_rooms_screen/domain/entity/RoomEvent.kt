package com.andersen.feature_rooms_screen.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoomEvent(
    val date: String,
    val description: String,
    val id: Long,
    val room: String,
    val colorRoom: Int,
    val roomId: Long,
    val startDateTime: String,
    val endDateTime: String,
    val status: String,
    val title: String,
    val userEmail: String,
    val userFullName: String,
    val userId: String,
    val userPosition: String,
    val userSkype: String,
    val isUserOwnEvent: Boolean,
): Parcelable