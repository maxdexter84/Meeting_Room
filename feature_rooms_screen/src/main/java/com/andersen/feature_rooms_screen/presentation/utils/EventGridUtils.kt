package com.andersen.feature_rooms_screen.presentation.utils

import android.os.Parcelable
import android.view.View
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import com.example.core_module.utils.TimeUtilsConstants.DATE_FORMAT_FOR_API
import com.example.core_module.utils.TimeUtilsConstants.END_INDEX
import com.example.core_module.utils.TimeUtilsConstants.END_WORK_TIME_IN_OFFICE
import com.example.core_module.utils.TimeUtilsConstants.OFFICE_WORKING_TIME_IN_MINUTES
import com.example.core_module.utils.TimeUtilsConstants.START_INDEX
import com.example.core_module.utils.TimeUtilsConstants.START_WORK_TIME_IN_OFFICE
import com.example.core_module.utils.TimeUtilsConstants.TIME_DATE_FORMAT
import com.example.core_module.utils.stringToDateTime
import com.example.core_module.utils.stringToTime
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.parcel.Parcelize
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
            isUserOwnEvent = it.isUserOwnEvent,
            date = it.date,
            room = it.room,
            userEmail = it.userEmail,
            userSkype = it.userSkype,
            description = it.description,
            startDateTime = it.startDateTime,
            endDateTime = it.endDateTime,
            roomId = it.roomId,
            roomTitle = it.room
        )
    }

fun List<RoomEvent>.toEmptyEventListForGrid(
    heightSingleRoomGrid: Int,
    currentDate: CalendarDay
): List<EmptyRoomEventForGrid> =
    if (isEmpty()) {
        val date = currentDate.toApiDate()
        val second = ":00"
        val zone = ZonedDateTime.now().format(DateTimeFormatter.ofPattern(""))
        listOf(
            EmptyRoomEventForGrid(
                startTime = "${date}T$START_WORK_TIME_IN_OFFICE$second$zone",
                endTime = "${date}T$END_WORK_TIME_IN_OFFICE$second$zone",
                heightEventItem = heightSingleRoomGrid
            )
        )
    } else {
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
                startTime = list[0].startDateTime.replaceRange(START_INDEX, END_INDEX, START_WORK_TIME_IN_OFFICE),
                endTime = list[0].startDateTime,
                heightEventItem = calculateHeightEventItem(
                    Duration.between(
                        list[0].startDateTime.replaceRange(START_INDEX, END_INDEX, START_WORK_TIME_IN_OFFICE)
                            .stringToDateTime(TIME_DATE_FORMAT),
                        list[0].startDateTime.stringToDateTime(TIME_DATE_FORMAT)
                    ).toMinutes(),
                    heightSingleRoomGrid
                )
            )
        }
        list.size -> {
            EmptyRoomEventForGrid(
                startTime = list.last().endDateTime,
                endTime = list.last().endDateTime.replaceRange(START_INDEX, END_INDEX, END_WORK_TIME_IN_OFFICE),
                heightEventItem = calculateHeightEventItem(
                    Duration.between(
                        list.last().endDateTime.stringToDateTime(TIME_DATE_FORMAT),
                        list.last().endDateTime.replaceRange(START_INDEX, END_INDEX, END_WORK_TIME_IN_OFFICE)
                            .stringToDateTime(TIME_DATE_FORMAT),
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

private fun checkTitleVisibility(minuteInterval: Long) =
    if (minuteInterval < 30) View.GONE else View.VISIBLE

private fun checkTitleLength(minuteInterval: Long, title: String) =
    if (minuteInterval in 30..59 && title.length > 30) "${title.substring(0, 30)}..." else title

private fun calculateHeightEventItem(minuteInterval: Long, heightSingleRoomGrid: Int) =
    (heightSingleRoomGrid * (minuteInterval.toDouble() / OFFICE_WORKING_TIME_IN_MINUTES)).roundToInt()

private fun calculateMinuteInterval(roomEvent: RoomEvent) = Duration.between(
    roomEvent.startDateTime.stringToTime(TIME_DATE_FORMAT),
    roomEvent.endDateTime.stringToTime(TIME_DATE_FORMAT)
).toMinutes()

@Parcelize
data class RoomEventForGrid(
    val userFullName: String,
    val userPosition: String,
    val colorRoom: Int,
    val title: String,
    val heightEventItem: Int,
    val titleVisibility: Int,
    val isUserOwnEvent: Boolean,
    val roomId: Long,
    val roomTitle: String,
    val date: String,
    val room: String,
    val userEmail: String,
    val userSkype: String,
    val description: String,
    val startDateTime: String,
    val endDateTime: String,
) : Parcelable

data class EmptyRoomEventForGrid(
    val startTime: String,
    val endTime: String,
    val heightEventItem: Int,
)

fun CalendarDay.toApiDate(): String {
    val formatter = org.threeten.bp.format.DateTimeFormatter.ofPattern(DATE_FORMAT_FOR_API)
    return date.format(formatter)
}
