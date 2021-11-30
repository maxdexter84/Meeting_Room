package com.meeringroom.ui.event_dialogs.dialog_room_picker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoomPickerNewEventData(
    val room: String,
    var isSelected: Boolean,
    var isEnabled: Boolean,
): Parcelable
