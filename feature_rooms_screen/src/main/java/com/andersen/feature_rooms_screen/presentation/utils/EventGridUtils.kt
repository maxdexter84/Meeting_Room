package com.andersen.feature_rooms_screen.presentation.utils

import android.view.View
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import com.example.core_module.utils.TimeUtilsConstants.END_WORK_TIME_IN_OFFICE
import com.example.core_module.utils.TimeUtilsConstants.OFFICE_WORKING_TIME_IN_MINUTES
import com.example.core_module.utils.TimeUtilsConstants.START_WORK_TIME_IN_OFFICE
import com.example.core_module.utils.TimeUtilsConstants.TIME_DATE_FORMAT
import com.example.core_module.utils.stringToDateTime
import com.example.core_module.utils.stringToTime
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

fun List<RoomEvent>.toEventListForGrid(heightSingleRoomGrid: Int) =
    sortedBy { it.startDateTime }.map {
        val minuteInterval = calculateMinuteInterval(it)
        RoomEventForGrid(
            userFullName = it.userFullName,
            userPosition = it.userPosition,
            colorRoom = it.colorRoom,
            title = checkTitleLength(minuteInterval, it.title),
            heightEventItem = calculateHeightEventItem(minuteInterval, heightSingleRoomGrid),
            titleVisibility = checkTitleVisibility(minuteInterval),
            isUserOwnEvent = it.isUserOwnEvent
        )
    }

fun List<RoomEvent>.toEmptyEventListForGrid(heightSingleRoomGrid: Int,  currentDate: CalendarDay): List<EmptyRoomEventForGrid> =
    if (isEmpty())
        listOf(
        EmptyRoomEventForGrid(
        startTime = "${currentDate.toString().substring(12,22)}T$START_WORK_TIME_IN_OFFICE:00.${ZonedDateTime.now().format(DateTimeFormatter.ofPattern("SSSXXX"))}",
        endTime = "${currentDate.toString().substring(12,22)}T$END_WORK_TIME_IN_OFFICE:00.${ZonedDateTime.now().format(DateTimeFormatter.ofPattern("SSSXXX"))}",
        heightEventItem = heightSingleRoomGrid
        ))
    else {
        val sortedEventList = sortedBy { it.startDateTime }
        val listEmptyEvent = mutableListOf<EmptyRoomEventForGrid>()
        for (index in 0..size) {
            listEmptyEvent.add(
                calculateEmptyEventItem(
                    index, sortedEventList, heightSingleRoomGrid
                )
            )
        }
        listEmptyEvent.toList()
    }

private fun calculateEmptyEventItem(index: Int, list: List<RoomEvent>, heightSingleRoomGrid: Int) =
    when (index) {
        0 -> {
            EmptyRoomEventForGrid(
                startTime = list[0].startDateTime.replaceRange(11, 16, START_WORK_TIME_IN_OFFICE),
                endTime = list[0].startDateTime,
                heightEventItem = calculateHeightEventItem(
                    Duration.between(
                        list[0].startDateTime.replaceRange(11, 16, START_WORK_TIME_IN_OFFICE).stringToDateTime(TIME_DATE_FORMAT),
                        list[0].startDateTime.stringToDateTime(TIME_DATE_FORMAT)
                    ).toMinutes(),
                    heightSingleRoomGrid
                )
            )
        }
        list.size -> {
            EmptyRoomEventForGrid(
                startTime = list.last().endDateTime,
                endTime = list.last().endDateTime.replaceRange(11, 16, END_WORK_TIME_IN_OFFICE),
                heightEventItem = calculateHeightEventItem(
                    Duration.between(
                        list.last().endDateTime.stringToDateTime(TIME_DATE_FORMAT),
                        list.last().endDateTime.replaceRange(11, 16, END_WORK_TIME_IN_OFFICE).stringToDateTime(TIME_DATE_FORMAT),
                    ).toMinutes() + 1,
                    heightSingleRoomGrid
                )
            )
        }
        else -> {
            EmptyRoomEventForGrid(
                startTime = list[(index - 1)].endDateTime,
                endTime = list[(index)].endDateTime,
                heightEventItem =
                calculateHeightEventItem(
                    Duration.between(
                        list[(index - 1)].endDateTime.stringToTime(TIME_DATE_FORMAT),
                        list[(index)].startDateTime.stringToTime(TIME_DATE_FORMAT)
                    ).toMinutes(),
                    heightSingleRoomGrid
                )
            )
        }
    }

private fun checkTitleVisibility(minuteInterval: Long) = if (minuteInterval < 30) View.GONE else View.VISIBLE

private fun checkTitleLength(minuteInterval: Long, title: String) = if (minuteInterval in 30..59 && title.length > 30) "${title.substring(0, 30)}..." else title

private fun calculateHeightEventItem(minuteInterval: Long, heightSingleRoomGrid: Int) =
    (heightSingleRoomGrid * (minuteInterval.toDouble() / OFFICE_WORKING_TIME_IN_MINUTES)).roundToInt()

private fun calculateMinuteInterval(roomEvent: RoomEvent) = Duration.between(
    roomEvent.startDateTime.stringToTime(TIME_DATE_FORMAT),
    roomEvent.endDateTime.stringToTime(TIME_DATE_FORMAT)
).toMinutes()

data class RoomEventForGrid(
    val userFullName: String,
    val userPosition: String,
    val colorRoom: Int,
    val title: String,
    val heightEventItem: Int,
    val titleVisibility: Int,
    val isUserOwnEvent: Boolean,
)

data class EmptyRoomEventForGrid(
    val startTime: String,
    val endTime: String,
    val heightEventItem: Int,
)