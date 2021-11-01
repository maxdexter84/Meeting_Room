package com.andersen.feature_rooms_screen.presentation.rooms_event_grid

import android.graphics.Color
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent

class GagForRoomEvents {

    fun generateRoomEvents(size: Int): List<RoomEvent> {
        val list = ArrayList<RoomEvent>()
        for (i in 0 until size) {

            val date = "8 Sep 2021"

            val description = when (i % 9) {
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


            val title = when (i % 9) {
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

            val room = when (i % 9) {
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

            val startDateTime = when (i % 9) {
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

            val endDateTime = when (i % 9) {
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

            val status = when (i % 9) {
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

            val userFullName = when (i % 9) {
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

            val userPosition = when (i % 9) {
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

            val userEmail = when (i % 9) {
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

            val userSkype = when (i % 9) {
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

            val userId = when (i % 9) {
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

            val color = when (i % 9) {
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

            val userEvent = when (i % 9) {
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

            list.add(
                RoomEvent(
                    date,
                    description,
                    i.toLong(),
                    room,
                    color,
                    i.toLong(),
                    startDateTime,
                    endDateTime,
                    status,
                    title,
                    userEmail,
                    userFullName,
                    userId,
                    userPosition,
                    userSkype,
                    userEvent
                )
            )
        }

        return list
    }
}