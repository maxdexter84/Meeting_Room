package com.meetingroom.andersen.feature_landing.data

import com.example.core_network.RequestResult
import com.meetingroom.andersen.feature_landing.domain.entity.ChangedEventDTO
import com.meetingroom.andersen.feature_landing.domain.entity.DateTimeBody
import com.meetingroom.andersen.feature_landing.domain.entity.StatusRoomsDTO
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
            status = STATUS_OCCUOIED
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

    companion object {
        const val DESCRIPTION = "Some description"
        const val END_DATE_TIME = "2022-01-31T15:20:00"
        const val START_DATE_TIME = "2022-01-31T15:20:00"
        const val TITLE_FIRST_ROOM = "first room"
        const val TITLE_SECOND_ROOM = "second room"
        const val TITLE_EVENT_CHANGED = "some title"
        const val STATUS_FREE = "FREE"
        const val STATUS_OCCUOIED = "OCCUPIED"
        const val ID_CHANGED_EVENT = 123L
        const val ID_ROOM_CHANGED = 4L
        const val ID_EVENT = 123L
        const val ID_FIRST_ROOM = 1L
        const val ID_SECOND_ROOM = 2L
    }
}