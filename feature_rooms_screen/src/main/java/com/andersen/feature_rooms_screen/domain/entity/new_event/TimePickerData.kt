package com.andersen.feature_rooms_screen.domain.entity.new_event

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimePickerData(
    var title: String,
    var time: Int,
    var isSelected: Boolean,
) :Parcelable
