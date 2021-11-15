package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimePickerData(
    var title: String,
    var time: Int,
    var isSelected: Boolean,
) :Parcelable
