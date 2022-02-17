package com.andersen.feature_rooms_screen.data

import com.andersen.feature_rooms_screen.domain.entity.remote.*
import com.example.core_network.RequestResult
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import org.mockito.Mockito

class RoomsScreenRepositoryTest {

    private val roomsScreenRepository = Mockito.mock(RoomsScreenRepository::class.java)
    private val activateRoomEventDto = Mockito.mock(ActivateRoomEventDto::class.java)
    private val createEventDTO = Mockito.mock(RoomEventCreateDto::class.java)
    private val roomsEventResponseDTO = Mockito.mock(RoomEventResponseDto::class.java)
    private val dateDTO = Mockito.mock(DateDTO::class.java)
    private val listRooms: List<RoomStatusDTO> = listOf()

    @After
    fun tearDown() {
        Mockito.reset(roomsScreenRepository)
    }

    @Test
    fun shouldSuccessCreateEvent() {
        Mockito.`when`(runBlocking {
            roomsScreenRepository.createEvent(createEventDTO)
        }).thenReturn(RequestResult.Success(roomsEventResponseDTO))

        val actual = runBlocking {
            roomsScreenRepository.createEvent(createEventDTO)
        }
        val expected = RequestResult.Success(roomsEventResponseDTO)

        assertEquals(expected, actual)
    }


    @Test
    fun shouldSuccessActivateEvent() {
        Mockito.`when`(runBlocking {
            roomsScreenRepository.activateEvent(activateRoomEventDto)
        }).thenReturn(RequestResult.Success(Unit))

        val actual = runBlocking {
            roomsScreenRepository.activateEvent(activateRoomEventDto)
        }
        val expected = RequestResult.Success(Unit)

        assertEquals(expected, actual)
    }

    @Test
    fun shouldSuccessGetFreeRooms() {
        Mockito.`when`(runBlocking {
            roomsScreenRepository.getFreeRooms(dateDTO)
        }).thenReturn(RequestResult.Success(listRooms))

        val actual = runBlocking {
            roomsScreenRepository.getFreeRooms(dateDTO)
        }
        val expected = RequestResult.Success(listRooms)

        assertEquals(expected, actual)
    }
}