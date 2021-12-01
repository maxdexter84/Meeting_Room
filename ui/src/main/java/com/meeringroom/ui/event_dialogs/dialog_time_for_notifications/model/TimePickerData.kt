package com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimePickerData(
    var title: String,
    var time: Int,
    var isSelected: Boolean,
) :Parcelable
