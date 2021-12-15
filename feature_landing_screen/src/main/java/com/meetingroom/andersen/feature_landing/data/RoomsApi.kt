package com.meetingroom.andersen.feature_landing.data

import com.meeringroom.ui.event_dialogs.dialog_room_picker.model.RoomPickerNewEventData
import com.meetingroom.andersen.feature_landing.domain.entity.HistoryEventData
import com.meetingroom.andersen.feature_landing.domain.entity.UpcomingEventData

interface RoomsApi {

    fun getRoomPickerNewEventData(): Array<RoomPickerNewEventData>
    suspend fun getUpcomingEventData(): List<UpcomingEventData>
    fun getGagForHistoryEvents(): List<HistoryEventData>
}