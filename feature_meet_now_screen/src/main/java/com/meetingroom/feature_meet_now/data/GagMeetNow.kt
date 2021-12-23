package com.meetingroom.feature_meet_now.data

import android.graphics.Color
import com.meetingroom.feature_meet_now.domain.entity.Room
import javax.inject.Inject

class GagMeetNow @Inject constructor(): RoomsApi {

    override fun getRoomsAvailableNow(): List<Room> {
        val data = ArrayList<Room>()
        data.add(
            Room(
                5,
                "Gray",
                Color.GRAY,
                "11:00",
                "12:00"
            )
        )
        data.add(
            Room(
                6,
                "Blue",
                Color.BLUE,
                "09:00",
                "10:30"
            )
        )
        data.add(
            Room(
                4,
                "Green",
                Color.GREEN,
                "16:30",
                "17:00"
            )
        )
        data.add(
            Room(
                3,
                "Black",
                Color.BLACK,
                "17:00",
                "18:00"
            )
        )
        data.add(
            Room(
                2,
                "Drkgray",
                Color.DKGRAY,
                "12:00",
                "13:00"
            )
        )
        data.add(
            Room(
                2,
                "Magenta",
                Color.MAGENTA,
                "14:00",
                "16:00"
            )
        )
        data.add(
            Room(
                2,
                "Red",
                Color.RED,
                "09:00",
                "11:00"
            )
        )
        data.add(
            Room(
                2,
                "Yellow",
                Color.YELLOW,
                "11:30",
                "12:40"
            )
        )
        return data
    }
}