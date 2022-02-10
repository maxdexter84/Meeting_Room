package com.meeringroom.ui.event_dialogs.dialog_floor_picker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FloorData(
    var floorName: String,
    var numberOfPlaces: Int,
    var isSelected: Boolean = false
): Parcelable
