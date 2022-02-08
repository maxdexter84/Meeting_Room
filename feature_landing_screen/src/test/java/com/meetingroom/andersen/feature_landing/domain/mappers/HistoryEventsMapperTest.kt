package com.meetingroom.andersen.feature_landing.domain.mappers

import com.meetingroom.andersen.feature_landing.data.*
import com.meetingroom.andersen.feature_landing.domain.entity.HistoryEventData
import com.meetingroom.andersen.feature_landing.domain.entity.HistoryEventDataDTO
import junit.framework.Assert.assertEquals
import org.junit.Test

class HistoryEventsMapperTest {

    @Test
    fun shouldReturnHistoryEventsList() {
        val historyEventDTO =
            HistoryEventDataDTO(
                id = ID,
                title = TITLE,
                description = DESCRIPTION,
                startDateTime = START_DATE_TIME,
                endDateTime = END_DATE_TIME,
                roomId = ROOM_ID,
                room = ROOM,
                userFullName = USER_FULL_NAME,
                userId = USER_ID,
                userPosition = USER_POSITION,
                userEmail = USER_EMAIL,
                userSkype = USER_SKYPE,
                status = STATUS,
                color = COLOR
            )
        val eventsList = listOf(historyEventDTO, historyEventDTO)

        val actual = eventsList.toHistoryEventsList()

        val expectedEvent =
            HistoryEventData(
                id = ID,
                title = TITLE,
                description = DESCRIPTION,
                startDateTime = START_DATE_TIME,
                endDateTime = END_DATE_TIME,
                roomId = ROOM_ID,
                room = ROOM,
                userFullName = USER_FULL_NAME,
                userId = USER_ID,
                userPosition = USER_POSITION,
                userEmail = USER_EMAIL,
                userSkype = USER_SKYPE,
                status = STATUS,
                color = COLOR,
                startTime = START_TIME,
                endTime = END_TIME,
                eventDate = EVENT_DATE
            )

        val expected = listOf(expectedEvent, expectedEvent)

        assertEquals(expected, actual)
    }
}