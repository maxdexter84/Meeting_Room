package com.meetingroom.andersen.feature_landing.history_of_events_fragment.presentation

import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.history_of_events_fragment.model.HistoryEventData
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.model.UpcomingEventData

class GagForHistoryEvents {
    fun generateData(size: Int): List<HistoryEventData> {
        var list = ArrayList<HistoryEventData>()
        for (i in 0 until size) {
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

            val startTime = when (i % 9) {
                0 -> "11.00"
                1 -> "9.00"
                2 -> "16.30"
                3 -> "17.00"
                4 -> "12.00"
                5 -> "14.00"
                6 -> "9.00"
                7 -> "11.30"
                else -> "15.30"
            }

            val endTime = when (i % 9) {
                0 -> "12.00"
                1 -> "10.30"
                2 -> "17.00"
                3 -> "18.00"
                4 -> "13.00"
                5 -> "16.00"
                6 -> "11.00"
                7 -> "12.40"
                else -> "16.30"
            }

            val eventDate = when (i % 9) {
                0 -> "8 sept 2021"
                1 -> "8 sept 2021"
                2 -> "7 sept 2021"
                3 -> "6 sept 2021"
                4 -> "5 sept 2021"
                5 -> "5 sept 2021"
                6 -> "4 sept 2021"
                7 -> "4 sept 2021"
                else -> "2 sept 2021"
            }

            val eventRoom = when (i % 9) {
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

            val eventRoomColour = when (i % 9) {
                0 -> R.color.design_default_color_primary
                1 -> R.color.purple_light
                2 -> R.color.purple_dark
                3 -> R.color.purple
                4 -> R.color.teal_dark
                5 -> R.color.yellow_for_selected_item
                6 -> R.color.error_color_for_line_under_edittext
                7 -> R.color.buttons_text_disabled
                else -> R.color.teal_light
            }

            val eventBellActive = when (i % 2) {
                0 -> false
                else -> true
            }
            val bookerName = when (i % 9) {
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
            val bookerPosition = when (i % 9) {
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
            val bookerEmail = when (i % 9) {
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

            val bookerSkype = when (i % 9) {
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

            list.add(
                HistoryEventData(
                    title,
                    startTime,
                    endTime,
                    eventDate,
                    eventRoom,
                    eventRoomColour,
                    bookerName,
                    bookerPosition,
                    bookerEmail,
                    bookerSkype,
                    description
                )
            )
        }

        return list
    }
}