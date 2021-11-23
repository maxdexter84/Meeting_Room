package com.andersen.feature_rooms_screen.presentation.rooms_event_grid

import android.graphics.Color
import com.andersen.feature_rooms_screen.data.RoomsApi
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import javax.inject.Inject

class GagForRoomsScreen @Inject constructor() : RoomsApi {

    override fun getRooms(): List<Room> {
        val list = ArrayList<Room>()
        for (i in 0 until SIZE_ROOM) {
            list += when (i % 9) {

                0 -> Room(5, 8, 0, "Dnipro", "Gray", Color.GRAY)
                1 -> Room(6, 8, 1, "Odessa", "Blue", Color.BLUE)
                2 -> Room(4, 8, 2, "Kyiv", "Green", Color.GREEN)
                3 -> Room(3, 8, 3, "Moscow", "Black", Color.BLACK)
                4 -> Room(2, 8, 4, "Piter", "Drkgray", Color.DKGRAY)
                5 -> Room(2, 8, 5, "Kazan", "Magenta", Color.MAGENTA)
                6 -> Room(2, 8, 6, "Vladivostok", "Red", Color.RED)
                7 -> Room(2, 8, 7, "Minsk", "Yellow", Color.YELLOW)
                else -> Room(2, 8, 9, "Gomel", "Green", Color.GREEN)
            }
        }
        return list
    }

    override fun getRoomEventsByRoom(): List<RoomEvent> {
        val list = ArrayList<RoomEvent>()
        for (i in 0 until 1) {
            list.add(
                RoomEvent(
                    date = getDate(),
                    description = getDescription(i),
                    id = i.toLong(),
                    room = "Blue",
                    colorRoom = Color.BLUE,
                    roomId = 1,
                    startDateTime = "2021-11-16T11:00:00.777Z",
                    endDateTime = "2021-11-16T12:00:00.777Z",
                    status = getStatus(i),
                    title = getTitle(i),
                    userEmail = getUserEmail(i),
                    userFullName = getUserFullName(i),
                    userId = getUserId(i),
                    userPosition = getUserPosition(i),
                    userSkype = getUserSkype(i),
                    isUserOwnEvent = getUserEvent(i)
                )
            )
        }
        return list
    }

    override fun getRoomEvents(): List<RoomEvent> {
        val list = ArrayList<RoomEvent>()
        for (i in 0 until SIZE_EVENTS) {
            list.add(
                RoomEvent(
                    date = getDate(),
                    description = getDescription(i),
                    id = i.toLong(),
                    room = getRoom(i),
                    colorRoom = getEventsColor(i),
                    roomId = i.toLong(),
                    startDateTime = getStartDateTime(i),
                    endDateTime = getEndDateTime(i),
                    status = getStatus(i),
                    title = getTitle(i),
                    userEmail = getUserEmail(i),
                    userFullName = getUserFullName(i),
                    userId = getUserId(i),
                    userPosition = getUserPosition(i),
                    userSkype = getUserSkype(i),
                    isUserOwnEvent = getUserEvent(i)
                )
            )
        }
        return list
    }

    private fun getUserEvent(i: Int) =
        when (i % 9) {
            0 -> true
            1 -> false
            2 -> true
            3 -> false
            4 -> false
            5 -> true
            6 -> false
            7 -> true
            else -> false
        }

    private fun getEventsColor(i: Int) =
        when (i % 9) {
            0 -> Color.GRAY
            1 -> Color.BLUE
            2 -> Color.GREEN
            3 -> Color.BLACK
            4 -> Color.DKGRAY
            5 -> Color.MAGENTA
            6 -> Color.RED
            7 -> Color.YELLOW
            else -> Color.GREEN
        }

    private fun getUserId(i: Int) =
        when (i % 9) {
            0 -> "0"
            1 -> "1"
            2 -> "2"
            3 -> "3"
            4 -> "4"
            5 -> "5"
            6 -> "6"
            7 -> "7"
            else -> "8"
        }

    private fun getUserSkype(i: Int) =
        when (i % 9) {
            0 -> "a.alexandrov"
            1 -> "nick-dick"
            2 -> "Micle"
            3 -> "gangsta"
            4 -> "moroz"
            5 -> "qwerty"
            6 -> "apple"
            7 -> "norm"
            else -> "cool"
        }

    private fun getUserEmail(i: Int) =
        when (i % 9) {
            0 -> "a.alexandrov"
            1 -> "nick-dick"
            2 -> "Micle"
            3 -> "gangsta"
            4 -> "moroz"
            5 -> "qwerty"
            6 -> "apple"
            7 -> "norm"
            else -> "cool"
        } + "@andersenlab.com"

    private fun getUserPosition(i: Int) =
        when (i % 9) {
            0 -> "PM"
            1 -> "Android developer"
            2 -> "JS developer"
            3 -> "Angular developer"
            4 -> "RN developer"
            5 -> "Recruiter"
            6 -> "HR"
            7 -> "Recruiter"
            else -> "BA"
        }

    private fun getUserFullName(i: Int) =
        when (i % 9) {
            0 -> "Alexandrov Alexandr"
            1 -> "Nick Jonson"
            2 -> "Micle Baskatov"
            3 -> "Makson Masson"
            4 -> "Aleksei Moroz"
            5 -> "Qwerty Zerta"
            6 -> "Kata Abloko"
            7 -> "Ulia Normalnaya"
            else -> "Dasha Nerialnaya"
        }

    private fun getStatus(i: Int) =
        when (i % 9) {
            0 -> "status1"
            1 -> "status1"
            2 -> "status2"
            3 -> "status3"
            4 -> "status4"
            5 -> "status5"
            6 -> "status6"
            7 -> "status7"
            else -> "status8"
        }

    private fun getEndDateTime(i: Int) =
        when (i % 9) {
            0 -> "12:00"
            1 -> "10:30"
            2 -> "17:00"
            3 -> "18:00"
            4 -> "13:00"
            5 -> "16:00"
            6 -> "11:00"
            7 -> "12:40"
            else -> "11:30"
        }

    private fun getStartDateTime(i: Int) =
        when (i % 9) {
            0 -> "11:00"
            1 -> "09:00"
            2 -> "16:30"
            3 -> "17:00"
            4 -> "12:00"
            5 -> "14:00"
            6 -> "09:00"
            7 -> "11:30"
            else -> "09:30"
        }

    private fun getRoom(i: Int) =
        when (i % 9) {
            0 -> "Dnipro"
            1 -> "Odessa"
            2 -> "Kyiv"
            3 -> "Moscow"
            4 -> "Piter"
            5 -> "Kazan"
            6 -> "Vladivostok"
            7 -> "Minsk"
            else -> "Gomel"
        }

    private fun getTitle(i: Int) =
        when (i % 9) {
            0 -> "Retrospective - AlphaBank Home Insurance App"
            1 -> "Poker planing - AlphaBank app"
            2 -> "Chill - Android Space EPAM"
            3 -> "Assessment - Natural Naturalivich"
            4 -> "Retrospective - CentroBank Home Insurance App"
            5 -> "Interview"
            6 -> "Tinkoff Assessment"
            7 -> "Retrospective - RSHB Home Insurance App"
            else -> "Assessment - Arnold Petrov"
        }

    private fun getDescription(i: Int) =
        when (i % 9) {
            0 -> "I miss. This is the only explanation for my attraction to the past. " +
                    "No advice and psychotechnics help. You can only put up with it, " +
                    "try to get bored more and more quietly with the hope of someday learning to just remember."
            1 -> "I have always dreamed of seeing in her eyes the love that is in mine. " +
                    "And today, at last, I saw her. But she is not for me ..."
            2 -> "Some will hate you, some will love you, but most people absolutely " +
                    "don't give a damn about who you are, what you live for, " +
                    "and what ideas you are passionate about."
            3 -> "The history of the majority has an end, but the history " +
                    "of the minority will end only with the Universe ..."
            else -> "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
        }

    private fun getDate() = "8 Sep 2021"

    companion object {
        const val SIZE_EVENTS = 20
        const val SIZE_ROOM = 8
    }
}