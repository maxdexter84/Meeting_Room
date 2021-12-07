package com.meetingroom.andersen.feature_landing.data

import com.meeringroom.ui.event_dialogs.dialog_room_picker.model.RoomPickerNewEventData
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.domain.entity.HistoryEventData
import com.meetingroom.andersen.feature_landing.domain.entity.UpcomingEventData
import javax.inject.Inject

class GagLanding @Inject constructor() : RoomsApi {
    private val arraySize = 8

    override fun getGagForHistoryEvents(): List<HistoryEventData> {
        var list = ArrayList<HistoryEventData>()
        for (i in 0 until 9) {
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
                0 -> "11:00"
                1 -> "09:00"
                2 -> "16:30"
                3 -> "17:00"
                4 -> "12:00"
                5 -> "14:00"
                6 -> "09:00"
                7 -> "11:30"
                else -> "15:30"
            }

            val endTime = when (i % 9) {
                0 -> "12:00"
                1 -> "10:30"
                2 -> "17:00"
                3 -> "18:00"
                4 -> "13:00"
                5 -> "16:00"
                6 -> "11:00"
                7 -> "12:40"
                else -> "16:30"
            }

            val eventDate = when (i % 9) {
                0 -> "8 Sep 2021"
                1 -> "8 Sep 2021"
                2 -> "7 Sep 2021"
                3 -> "6 Sep 2021"
                4 -> "5 Sep 2021"
                5 -> "5 Sep 2021"
                6 -> "4 Sep 2021"
                7 -> "4 Sep 2021"
                else -> "2 Sep 2021"
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

    override fun getRoomPickerNewEventData(): Array<RoomPickerNewEventData> {
        val array = Array(arraySize) { i ->
            val room = when (i) {
                0 -> "Gray"
                1 -> "Blue"
                2 -> "Green"
                3 -> "Black"
                4 -> "Drkgray"
                5 -> "Magenta"
                6 -> "Red"
                7 -> "Yellow"
                else -> "Green"
            }

            val isSelected = false

            val isEnabled = when(i) {
                3,4 -> false
                else -> true
            }
            RoomPickerNewEventData(room, isSelected, isEnabled)
        }
        return array.sortedByDescending { room -> room.isEnabled }.toTypedArray()
    }

    override fun getUpcomingEventData(): List<UpcomingEventData> {
        val list = ArrayList<UpcomingEventData>()

        for (i in 0 until 9) {
            val title = when (i % 9) {
                0 -> "Retrospective - AlphaBank Home Insurance App"
                1 -> "Assessment - John Smith"
                2 -> "Retrospective - SberBank Home Insurance App"
                3 -> "Assessment - Ekaterina Ivanova"
                4 -> "Retrospective - CentroBank Home Insurance App"
                5 -> "Assessment - John Doe"
                6 -> "Assessment - Steven Willson"
                7 -> "Retrospective - RSHB Home Insurance App"
                else -> "Assessment - Arnold Petrov"
            }

            val startTime = when (i % 9) {
                0 -> "11:00"
                1 -> "09:00"
                2 -> "16:30"
                3 -> "17:00"
                4 -> "12:00"
                5 -> "14:00"
                6 -> "09:00"
                7 -> "11:30"
                else -> "15:30"
            }

            val endTime = when (i % 9) {
                0 -> "12:00"
                1 -> "10:30"
                2 -> "17:00"
                3 -> "18:00"
                4 -> "13:00"
                5 -> "16:00"
                6 -> "11:00"
                7 -> "12:40"
                else -> "16:30"
            }

            val eventDate = when (i % 9) {
                0 -> "7 Dec 2021"
                1 -> "21 May 2021"
                2 -> "18 Sep 2021"
                3 -> "14 Dec 2021"
                4 -> "17 Dec 2021"
                5 -> "3 Jun 2021"
                6 -> "19 Apr 2021"
                7 -> "20 Oct 2021"
                else -> "2 Jul 2021"
            }

            val eventRoom = when (i % 9) {
                0 -> "Red"
                1 -> "Orange"
                2 -> "Yellow"
                3 -> "Green"
                4 -> "Blue"
                5 -> "Gray"
                else -> "Purple"
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

            var reminderRemainingTime = when (i % 9) {
                0 -> "3d"
                1 -> "1m"
                2 -> "3m"
                3 -> "40m"
                4 -> "1h"
                5 -> "5h"
                6 -> "180m"
                7 -> "30d"
                else -> "Never"
            }

            val eventBellActive = when (i % 2) {
                0 -> false
                else -> true
            }

            val item = UpcomingEventData(
                title,
                startTime,
                endTime,
                eventDate,
                eventRoom,
                eventRoomColour,
                reminderRemainingTime,
                eventBellActive,
                null
            )

            list += item
        }
        return list
    }
}