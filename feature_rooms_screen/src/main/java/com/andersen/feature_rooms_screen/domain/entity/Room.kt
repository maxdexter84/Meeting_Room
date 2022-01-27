package com.andersen.feature_rooms_screen.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Room (
    val capacity: Int,
    val floor: Int,
    val id: Long,
    val office: String,
    val title: String,
    val color: String,
    val board: Boolean,
    val projector: Boolean
) :Parcelable