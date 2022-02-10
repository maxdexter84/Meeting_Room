package com.meetingroom.feature_workplaces_screen.presentation.mappers

import com.meeringroom.ui.event_dialogs.dialog_floor_picker.model.FloorData
import com.meetingroom.feature_workplaces_screen.domain.model.OfficeData

object Mappers {

    fun mapOfficeDataToFloorData(list: List<OfficeData>): List<FloorData> {
        return list.map {
            FloorData(it.floor, it.numberOfPlaces)
        }
    }
}