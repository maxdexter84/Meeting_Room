package com.meetingroom.feature_meet_now.presentation.utils

import com.meetingroom.feature_meet_now.domain.entity.Room

private const val NONE = 0
private const val MAX_BOOKING_TIME = 90

fun Room.getValidAvailableTime(): Int {
    return when (timeUntilNextEvent) {
        null,
        NONE,
        in (MAX_BOOKING_TIME + (availableIn ?: NONE))..Int.MAX_VALUE ->
            MAX_BOOKING_TIME
        else ->
            timeUntilNextEvent - (availableIn ?: NONE)
    }
}

fun Room.getValidTimeUntilNextEvent(): Int {
    return when (timeUntilNextEvent) {
        null,
        NONE,
        in (MAX_BOOKING_TIME + (availableIn ?: NONE))..Int.MAX_VALUE ->
            (availableIn ?: NONE) + MAX_BOOKING_TIME
        else ->
            timeUntilNextEvent
    }
}