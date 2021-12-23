package com.meetingroom.feature_meet_now.data

import com.meetingroom.feature_meet_now.domain.entity.Room

interface RoomsApi {
    fun getRoomsAvailableNow() : List<Room>
}