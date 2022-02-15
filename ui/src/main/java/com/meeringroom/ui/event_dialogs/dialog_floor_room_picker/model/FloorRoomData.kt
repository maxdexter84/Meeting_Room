package com.meeringroom.ui.event_dialogs.dialog_floor_room_picker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FloorRoomData(
    var roomName: String,
    var numberOfPlaces: Int,
    var isSelected: Boolean = false
): Parcelable