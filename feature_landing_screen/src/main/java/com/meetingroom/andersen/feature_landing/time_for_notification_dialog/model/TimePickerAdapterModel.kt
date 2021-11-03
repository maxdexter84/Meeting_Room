package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.model

sealed class TimePickerAdapterModel(val title : String, var isSelected: Boolean = false)
class Never(title: String, isSelected: Boolean = false) : TimePickerAdapterModel(title, isSelected)
class Specified(title: String,val time: Int, isSelected: Boolean = false) : TimePickerAdapterModel(title, isSelected)
class Custom(title: String, isSelected: Boolean = false) : TimePickerAdapterModel(title, isSelected)