package com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation

import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.model.UpcomingEventData

class GagForUpcomingEvents {

    fun generate(size: Int): List<UpcomingEventData> {
        val list = ArrayList<UpcomingEventData>()

        for (i in 0 until size) {
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

            val item = UpcomingEventData(
                title,
                startTime,
                endTime,
                eventDate,
                eventRoom,
                eventRoomColour,
                reminderActive = eventBellActive
            )

            list += item
        }
        return list
    }
}