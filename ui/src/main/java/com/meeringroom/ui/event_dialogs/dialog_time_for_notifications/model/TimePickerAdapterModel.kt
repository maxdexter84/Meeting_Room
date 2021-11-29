package com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.model

sealed class TimePickerAdapterModel(val title : String, var isSelected: Boolean = false)
class Never(title: String, isSelected: Boolean = false) : TimePickerAdapterModel(title, isSelected)
class Specified(title: String,val time: Int, isSelected: Boolean = false) : TimePickerAdapterModel(title, isSelected)
class Custom(title: String, isSelected: Boolean = false) : TimePickerAdapterModel(title, isSelected)