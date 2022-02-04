package com.meetingroom.andersen.feature_landing.domain.mappers

import com.example.core_module.utils.TimeUtilsConstants
import com.meetingroom.andersen.feature_landing.domain.entity.UpcomingEventData
import com.meetingroom.andersen.feature_landing.domain.entity.UpcomingEventDataDTO

private const val START_INDEX_FOR_TIME = 0
private const val END_INDEX_FOR_TIME = 5
private const val DIVIDER_DATE_AND_TIME = "T"

fun List<UpcomingEventDataDTO>.toUpcomingEventsList(): List<UpcomingEventData> {
    val upcomingEventList = mutableListOf<UpcomingEventData>()
    this.forEach {
        upcomingEventList.add(
            UpcomingEventData(
                id = it.roomId,
                title = it.title,
                description = it.description,
                startDateTime = it.startDateTime.substring(
                    TimeUtilsConstants.FIRST_CHAR_DATE_REMOTE,
                    TimeUtilsConstants.LAST_CHAR_DATE_REMOTE,
                ),
                endDateTime = it.endDateTime.substring(
                    TimeUtilsConstants.FIRST_CHAR_DATE_REMOTE,
                    TimeUtilsConstants.LAST_CHAR_DATE_REMOTE,
                ),
                roomId = it.roomId,
                room = it.room,
                userFullName = it.userFullName,
                userId = it.userId,
                userPosition = it.userPosition,
                userEmail = it.userEmail,
                userSkype = it.userSkype,
                status = it.status,
                startTime = it.startDateTime.split(DIVIDER_DATE_AND_TIME)[1].substring(
                    START_INDEX_FOR_TIME,
                    END_INDEX_FOR_TIME,
                ),
                endTime = it.endDateTime.split(DIVIDER_DATE_AND_TIME)[1].substring(
                    START_INDEX_FOR_TIME,
                    END_INDEX_FOR_TIME,
                ),
                eventDate = it.startDateTime.split(DIVIDER_DATE_AND_TIME)[0],
                color = it.color,
                reminderRemainingTime = null,
                reminderActive = false
            )
        )
    }
    return upcomingEventList
}
