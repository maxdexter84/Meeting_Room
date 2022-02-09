package com.andersen.feature_rooms_screen.domain.entity.mappers

import androidx.core.graphics.toColorInt
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import com.andersen.feature_rooms_screen.domain.entity.remote.RoomsEventDTO
import com.example.core_module.utils.TimeUtilsConstants.FIRST_CHAR_DATE_TIME_REMOTE
import com.example.core_module.utils.TimeUtilsConstants.LAST_CHAR_DATE
import com.example.core_module.utils.TimeUtilsConstants.LAST_CHAR_DATE_TIME_REMOTE

fun List<RoomsEventDTO>.toEventList(): List<RoomEvent> {
    val sortedEventList = sortedBy { it.id }
    val roomEventList = mutableListOf<RoomEvent>()
    for (index in 0 until size) {
        sortedEventList[index].events.forEach {
            roomEventList.add(
                RoomEvent(
                    id = it.roomId.toLong(),
                    title = it.title,
                    date = it.startDateTime.substring(FIRST_CHAR_DATE_TIME_REMOTE, LAST_CHAR_DATE),
                    description = it.description,
                    room = it.room,
                    colorRoom = it.color.toColorInt(),
                    roomId = it.roomId.toLong(),
                    startDateTime = it.startDateTime.substring(
                        FIRST_CHAR_DATE_TIME_REMOTE,
                        LAST_CHAR_DATE_TIME_REMOTE
                    ),
                    endDateTime = it.endDateTime.substring(
                        FIRST_CHAR_DATE_TIME_REMOTE,
                        LAST_CHAR_DATE_TIME_REMOTE
                    ),
                    status = it.status,
                    userEmail = it.userEmail,
                    userFullName = it.userFullName,
                    userId = it.userId,
                    userPosition = it.userPosition,
                    userSkype = it.userSkype,
                    isUserOwnEvent = true
                )
            )
        }
    }
    return roomEventList
}