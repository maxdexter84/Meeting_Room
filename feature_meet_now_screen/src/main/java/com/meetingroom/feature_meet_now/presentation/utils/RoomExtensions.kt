package com.meetingroom.feature_meet_now.presentation.utils

import com.meetingroom.feature_meet_now.domain.entity.Room
import com.meetingroom.feature_meet_now.presentation.fast_booking_fragment.Constants.MAX_EVENT_TIME

private const val NONE = 0

fun Room.getValidAvailableTime(limit: Int): Int {
    return when (timeUntilNextEvent) {
        null,
        NONE,
        in (MAX_EVENT_TIME + (availableIn ?: NONE))..Int.MAX_VALUE ->
            limit
        else ->
            timeUntilNextEvent - (availableIn ?: NONE)
    }
}

fun Room.getValidTimeUntilNextEvent(limit: Int): Int {
    return when (timeUntilNextEvent) {
        null,
        NONE,
        in (MAX_EVENT_TIME + (availableIn ?: NONE))..Int.MAX_VALUE ->
            (availableIn ?: NONE) + limit
        else ->
            timeUntilNextEvent
    }
}