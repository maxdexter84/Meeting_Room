package com.meetingroom.feature_workplaces_screen.presentation.mappers

import com.meeringroom.ui.event_dialogs.dialog_floor_picker.model.FloorData
import com.meeringroom.ui.event_dialogs.dialog_floor_room_picker.model.FloorRoomData
import com.meetingroom.feature_workplaces_screen.domain.model.OfficeData
import com.meetingroom.feature_workplaces_screen.domain.model.RoomData

object Mappers {

    fun mapOfficeDataToFloorData(list: List<OfficeData>): List<FloorData> {
        return list.map {
            FloorData(it.floor, it.numberOfPlaces)
        }
    }

    fun mapRoomDataToFloorRoomData(list: List<RoomData>): List<FloorRoomData> {
        return list.map {
            FloorRoomData(it.roomName, it.numberOfPlaces)
        }
    }
}