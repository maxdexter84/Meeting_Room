package com.meetingroom.feature_meet_now.presentation.fast_booking_fragment

import com.example.core_module.utils.*
import com.example.core_module.utils.TimeUtilsConstants.TIME_FORMAT
import com.meetingroom.feature_meet_now.domain.entity.Room
import com.meetingroom.feature_meet_now.presentation.fast_booking_fragment.Constants.MAX_EVENT_TIME
import com.meetingroom.feature_meet_now.presentation.fast_booking_fragment.Constants.MAX_TIME_RANGE
import com.meetingroom.feature_meet_now.presentation.fast_booking_fragment.Constants.START_TIME_LIMIT
import com.meetingroom.feature_meet_now.presentation.fast_booking_fragment.Constants.STEP
import com.meetingroom.feature_meet_now.presentation.utils.getValidAvailableTime
import com.meetingroom.feature_meet_now.presentation.utils.getValidTimeUntilNextEvent
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

private const val NONE = 0L

class RangeSliderAdapter(room: Room) {

    private var currentTime = LocalTime.now(ZoneOffset.UTC)

    val startLimitTimeString: String = currentTime
        .plusMinutes(room.availableIn?.toLong() ?: NONE)
        .roundUpMinute(STEP)
        .timeToString(TIME_FORMAT)

    val endLimitTimeString: String = currentTime
        .plusMinutes(room.getValidTimeUntilNextEvent(MAX_TIME_RANGE).toLong())
        .minusMinutes(
            if (room.getValidAvailableTime(MAX_EVENT_TIME) >= MAX_EVENT_TIME)
                (room.availableIn ?: NONE).toLong() + START_TIME_LIMIT - getLastValidStart()
            else NONE
        )
        .roundUpMinute(STEP)
        .timeToString(TIME_FORMAT)

    val sliderState: RangeSliderState = RangeSliderState(
        minutesFromCurrentTime(startLimitTimeString),
        getInitialEndLimit(),
        minutesFromCurrentTime(startLimitTimeString),
        getInitialEndSelected()
    )

    fun getLastValidStart(): Int {
        var lastValidStart = sliderState?.startLimit ?: minutesFromCurrentTime(startLimitTimeString)

        while (lastValidStart <= START_TIME_LIMIT - STEP) {
            lastValidStart += STEP
        }
        return lastValidStart
    }

    fun getSelectedTime(sliderPosition: Int): String {
        return currentTime.plusMinutes(sliderPosition.toLong()).timeToString(TIME_FORMAT)
    }

    private fun minutesFromCurrentTime(futureTime: String): Int {
        return ChronoUnit.MINUTES.between(
            LocalTime.now(ZoneOffset.UTC).timeToString(TIME_FORMAT).stringToTime(TIME_FORMAT),
            futureTime.stringToTime(TIME_FORMAT)
        ).toInt()
    }

    private fun getInitialEndSelected(): Int {
        return if (minutesFromCurrentTime(endLimitTimeString) - minutesFromCurrentTime(
                startLimitTimeString
            ) < MAX_EVENT_TIME
        ) {
            minutesFromCurrentTime(endLimitTimeString)
        } else {
            minutesFromCurrentTime(startLimitTimeString) + MAX_EVENT_TIME
        }
    }

    private fun getInitialEndLimit(): Int {
        return if (minutesFromCurrentTime(endLimitTimeString) - minutesFromCurrentTime(
                startLimitTimeString
            ) < MAX_TIME_RANGE
        ) {
            minutesFromCurrentTime(endLimitTimeString)
        } else {
            minutesFromCurrentTime(startLimitTimeString) + MAX_TIME_RANGE
        }
    }
}