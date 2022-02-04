package com.meetingroom.andersen.feature_landing.data

import com.example.core_network.RequestResult
import com.meetingroom.andersen.feature_landing.domain.entity.ChangedEventDTO
import com.meetingroom.andersen.feature_landing.domain.entity.DateTimeBody
import com.meetingroom.andersen.feature_landing.domain.entity.StatusRoomsDTO
import com.meetingroom.andersen.feature_landing.domain.entity.HistoryEventData
import com.meetingroom.andersen.feature_landing.domain.entity.UpcomingEventDataDTO
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class RoomsEventRepositoryTest {

    private val roomsEventRepository = mock(RoomsEventRepository::class.java)

    @After
    fun tearDown() {
        Mockito.reset(roomsEventRepository)
    }

    @Test
    fun shouldReturnUpcomingEventsSuccessfulFromRepository() {
        val event =
            UpcomingEventDataDTO(
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
        val testUpcomingEventDataList = listOf(event, event)
        Mockito.`when`(runBlocking {
            roomsEventRepository.getUpcomingEventData()
        }).thenReturn(RequestResult.Success(testUpcomingEventDataList))

        val actual = runBlocking {
            roomsEventRepository.getUpcomingEventData()
        }

        val testUpcomingEventDataListExpected = listOf(event, event)
        val expected = RequestResult.Success(testUpcomingEventDataListExpected)

        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnUpcomingEventsWithError() {
        Mockito.`when`(runBlocking {
            roomsEventRepository.getUpcomingEventData()
        }).thenReturn(RequestResult.Error(EXCEPTION, EXCEPTION_CODE))

        val actual = runBlocking {
            roomsEventRepository.getUpcomingEventData()
        }

        val expected = RequestResult.Error(EXCEPTION, EXCEPTION_CODE)

        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnHistoryEvents() {
        val event =
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

        val testHistoryEventsList = listOf(event, event)
        Mockito.`when`(runBlocking {
            roomsEventRepository.getHistoryEvents()
        }).thenReturn(RequestResult.Success(testHistoryEventsList))

        val actual = runBlocking {
            roomsEventRepository.getHistoryEvents()
        }

        val expected = RequestResult.Success(listOf(event, event))

        assertEquals(expected, actual)
    }

    @Test
    fun `should success changed event`() {
        val changeEvent = ChangedEventDTO(
            description = DESCRIPTION,
            endDateTime = END_DATE_TIME,
            id = ID_CHANGED_EVENT,
            roomId = ID_ROOM_CHANGED,
            startDateTime = START_DATE_TIME,
            title = TITLE_EVENT_CHANGED
        )
        Mockito.`when`(runBlocking {
            roomsEventRepository.putChangedEvent(changeEvent)
        }).thenReturn(Unit)

        val actual = runBlocking {
            roomsEventRepository.putChangedEvent(changeEvent)
        }
        val expected = Unit

        assertEquals(expected, actual)
    }

    @Test
    fun `should success changed event for admin`() {
        val changeEvent = ChangedEventDTO(
            description = DESCRIPTION,
            endDateTime = END_DATE_TIME,
            id = ID_CHANGED_EVENT,
            roomId = ID_ROOM_CHANGED,
            startDateTime = START_DATE_TIME,
            title = TITLE_EVENT_CHANGED
        )
        Mockito.`when`(runBlocking {
            roomsEventRepository.putChangedEventForAdmin(changeEvent)
        }).thenReturn(Unit)

        val actual = runBlocking {
            roomsEventRepository.putChangedEventForAdmin(changeEvent)
        }
        val expected = Unit

        assertEquals(expected, actual)
    }

    @Test
    fun `should success delete upcoming event`() {
        Mockito.`when`(runBlocking {
            roomsEventRepository.deleteUpcomingEvent(eventId = ID_EVENT)
        }).thenReturn(Unit)

        val actual = runBlocking {
            roomsEventRepository.deleteUpcomingEvent(ID_EVENT)
        }
        val expected = Unit

        assertEquals(expected, actual)
    }

    @Test
    fun `should success delete upcoming event for admin`() {
        Mockito.`when`(runBlocking {
            roomsEventRepository.deleteUpcomingEventForAdmin(eventId = ID_EVENT)
        }).thenReturn(Unit)

        val actual = runBlocking {
            roomsEventRepository.deleteUpcomingEventForAdmin(ID_EVENT)
        }
        val expected = Unit

        assertEquals(expected, actual)
    }

    @Test
    fun `should return list for new event data`() {
        val dataTimeBody =
            DateTimeBody(END_DATE_TIME, START_DATE_TIME)

        val roomFirstWithStatus = StatusRoomsDTO(
            id = ID_FIRST_ROOM,
            title = TITLE_FIRST_ROOM,
            status = STATUS_FREE
        )

        val roomSecondWithStatus = StatusRoomsDTO(
            id = ID_SECOND_ROOM,
            title = TITLE_SECOND_ROOM,
            status = STATUS_OCCUPIED
        )

        val testRoomsStatusList = listOf<StatusRoomsDTO>(roomFirstWithStatus, roomSecondWithStatus)
        Mockito.`when`(runBlocking {
            roomsEventRepository.getRoomPickerNewEventData(dataTimeBody)
        }).thenReturn(RequestResult.Success(testRoomsStatusList))

        val actual = runBlocking {
            roomsEventRepository.getRoomPickerNewEventData(dataTimeBody)
        }

        val testRoomStatusListExpected =
            listOf<StatusRoomsDTO>(roomFirstWithStatus, roomSecondWithStatus)
        val expected = RequestResult.Success(testRoomStatusListExpected)

        assertEquals(expected, actual)
    }
}