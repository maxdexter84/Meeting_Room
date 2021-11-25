package com.andersen.feature_rooms_screen.presentation.utils

import android.util.Log
import android.view.View
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import com.example.core_module.utils.TimeUtilsConstants
import com.example.core_module.utils.TimeUtilsConstants.OFFICE_WORKING_TIME_IN_MINUTES
import com.example.core_module.utils.TimeUtilsConstants.TIME_DATE_FORMAT
import com.example.core_module.utils.stringToTime
import java.time.Duration
import kotlin.math.roundToInt

fun List<RoomEvent>.toEventListForGrid(heightSingleRoomGrid: Int) =
    sortedBy { it.startDateTime }.map {
        val minuteInterval = calculateMinuteInterval(it)
        Log.d("aaa",
            minuteInterval.toString())
        RoomEventForGrid(
            userFullName = it.userFullName,
            userPosition = it.userPosition,
            colorRoom = it.colorRoom,
            title = checkTitleLength(minuteInterval, it.title),
            heightEventItem = calculateHeightEventItem(minuteInterval, heightSingleRoomGrid),
            titleVisibility = checkTitleVisibility(minuteInterval),
        )
    }

fun List<RoomEvent>.toEmptyEventListForGrid(heightSingleRoomGrid: Int) =
    if (isEmpty()) listOf(heightSingleRoomGrid)
    else {
        val sortedEventList = sortedBy { it.startDateTime }
        val listEmptyEvent = mutableListOf<Int>()
        for (index in 0..size) {
            listEmptyEvent.add(
                calculateHeightEmptyEventItem(index, sortedEventList, heightSingleRoomGrid)
            )
        }
        listEmptyEvent.toList()
    }

private fun calculateHeightEmptyEventItem(index: Int, list: List<RoomEvent>, heightSingleRoomGrid: Int) =
    when (index) {
        0 -> {
            Log.d("aaa",
                Duration.between(
                    TimeUtilsConstants.START_WORK_TIME_IN_OFFICE.stringToTime(TimeUtilsConstants.TIME_FORMAT),
                    list[0].startDateTime.stringToTime(TIME_DATE_FORMAT)
                ).toMinutes().toString())
            calculateHeightEventItem(
                Duration.between(
                    TimeUtilsConstants.START_WORK_TIME_IN_OFFICE.stringToTime(TimeUtilsConstants.TIME_FORMAT),
                    list[0].startDateTime.stringToTime(TIME_DATE_FORMAT)
                ).toMinutes(),
                heightSingleRoomGrid
            )
        }
        list.size -> {
            Log.d("aaa",
                Duration.between(
                    list.last().endDateTime.stringToTime(TIME_DATE_FORMAT),
                    TimeUtilsConstants.END_WORK_TIME_IN_OFFICE.stringToTime(TimeUtilsConstants.TIME_FORMAT)
                ).toMinutes().toString())
            calculateHeightEventItem(
                Duration.between(
                    list.last().endDateTime.stringToTime(TIME_DATE_FORMAT),
                    TimeUtilsConstants.END_WORK_TIME_IN_OFFICE.stringToTime(TimeUtilsConstants.TIME_FORMAT)
                ).toMinutes() + 2,
                heightSingleRoomGrid
            )
        }
        else -> {
            Log.d("aaa",
                Duration.between(
                    list[(index - 1)].endDateTime.stringToTime(TIME_DATE_FORMAT),
                    list[(index)].startDateTime.stringToTime(TIME_DATE_FORMAT)
                ).toMinutes().toString())
            calculateHeightEventItem(
                Duration.between(
                    list[(index - 1)].endDateTime.stringToTime(TIME_DATE_FORMAT),
                    list[(index)].startDateTime.stringToTime(TIME_DATE_FORMAT)
                ).toMinutes() ,
                heightSingleRoomGrid
            )
        }
    }

private fun checkTitleVisibility(minuteInterval: Long) = if (minuteInterval < 30) View.GONE else View.VISIBLE

private fun checkTitleLength(minuteInterval: Long, title: String) = if (minuteInterval in 30..59) "${title.substring(0, 30)}..." else title

private fun calculateHeightEventItem(minuteInterval: Long, heightSingleRoomGrid: Int): Int{
    val a = (heightSingleRoomGrid * (minuteInterval.toDouble() / OFFICE_WORKING_TIME_IN_MINUTES))

    Log.d("aaa",
           a.toString())
return a.roundToInt()
}

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
)