package com.meeringroom.ui.view.base_date_time_picker_fragment

import com.example.core_module.utils.TimeUtilsConstants
import com.example.core_module.utils.stringToTime
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class TimePickerDialogCreator(private val timeString: String) {
    fun create(): MaterialTimePicker {
        return with(timeString.stringToTime(TimeUtilsConstants.TIME_FORMAT)) {
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hour)
                .setMinute(minute)
                .build()
        }
    }
}