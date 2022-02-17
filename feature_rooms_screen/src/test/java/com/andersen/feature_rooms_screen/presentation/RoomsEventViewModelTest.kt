package com.andersen.feature_rooms_screen.presentation

import com.andersen.feature_rooms_screen.data.RoomsScreenRepository
import com.andersen.feature_rooms_screen.domain.entity.remote.DateDTO
import com.andersen.feature_rooms_screen.domain.entity.remote.RoomEventCreateDto
import com.example.core_module.event_time_validation.TimeValidationEvent
import com.example.core_module.event_time_validation.UserTimeValidationDialogManager
import com.example.core_module.state.State
import com.example.core_network.RequestResult
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

const val EXCEPTION = "Unauthorized"
const val EXCEPTION_CODE = 401
const val DESCRIPTION = "description"
const val START_DATE_TIME = "2022-01-31T12:20:00"
const val END_DATE_TIME = "2022-01-31T15:20:00"
const val ROOM_ID = 1L
const val TITLE = "title"

class RoomsEventViewModelTest {

    private lateinit var viewModel: RoomsEventViewModel
    private val dialogManager = Mockito.mock(UserTimeValidationDialogManager::class.java)
    private val roomsScreenRepository = Mockito.mock(RoomsScreenRepository::class.java)
    private val dateDTO = Mockito.mock(DateDTO::class.java)
    private val setEvent = Mockito.mock(TimeValidationEvent::class.java)
    private val createEvent = Mockito.mock(RoomEventCreateDto::class.java)
    private val viewModelMock = Mockito.mock(RoomsEventViewModel::class.java)

    @Before
    fun setup() {
        viewModel = RoomsEventViewModel(dialogManager, roomsScreenRepository)
    }

    @Test
    fun getFreeRoomsListLoadingState() {
        viewModel.getFreeRoomsList(START_DATE_TIME, END_DATE_TIME, ROOM_ID)
        val actual = viewModel.mutableState.value
        val expected = State.Loading
        assertEquals(expected, actual)
    }

    @Test
    fun getFreeRoomsListErrorState() {
        Mockito.`when`(runBlocking {
            roomsScreenRepository.getFreeRooms(dateDTO)
        }).thenReturn(RequestResult.Error(EXCEPTION, EXCEPTION_CODE))

        val response = runBlocking { roomsScreenRepository.getFreeRooms(dateDTO) }

        val actual = if (response == RequestResult.Error(EXCEPTION, EXCEPTION_CODE)) {
            State.Error
        } else {
            State.NotLoading
        }

        val expected = State.Error
        assertEquals(expected, actual)
    }

    @Test
    fun getFreeRoomsListTest() {
        Mockito.doNothing().`when`(viewModelMock)
            .getFreeRoomsList(START_DATE_TIME, END_DATE_TIME, ROOM_ID)
    }

    @Test
    fun activateEventTest() {
        Mockito.doNothing().`when`(viewModelMock)
            .activateEvent(DESCRIPTION, ROOM_ID, START_DATE_TIME, END_DATE_TIME, TITLE)
    }

    @Test
    fun setEventTest() {
        Mockito.doNothing().`when`(viewModelMock).setEvent(setEvent)
    }

    @Test
    fun createEventTest() {
        Mockito.doNothing().`when`(viewModelMock).createEvent(createEvent)
    }
}