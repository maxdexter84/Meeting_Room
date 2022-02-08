package com.meetingroom.feature_meet_now.presentation.fast_booking_fragment

import com.example.core_module.utils.addToStringTime
import com.example.core_module.utils.roundUpMinute
import com.example.core_module.utils.timeToString
import com.meetingroom.feature_meet_now.domain.entity.Room
import java.time.LocalTime

private const val MAX_TIME_RANGE = 120
private const val MIN_TO_ROUND = 5
private const val TIME_FORMAT = "HH:mm"

class RangeSliderAdapter(private val room: Room) {

    val startTimeLimit: String = LocalTime.now()
        .roundUpMinute(MIN_TO_ROUND)
        .timeToString(TIME_FORMAT)

    val endTimeLimit: String
        get() = startTimeLimit
            .addToStringTime(MAX_TIME_RANGE, TIME_FORMAT)

    fun getSelectedTime(sliderPosition: Float): String {
        return startTimeLimit.addToStringTime(sliderPosition.toInt(), TIME_FORMAT)
    }
}