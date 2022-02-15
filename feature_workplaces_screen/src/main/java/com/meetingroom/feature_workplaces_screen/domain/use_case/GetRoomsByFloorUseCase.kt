package com.meetingroom.feature_workplaces_screen.domain.use_case

import com.example.core_network.RequestResult
import com.meetingroom.feature_workplaces_screen.domain.model.RoomData

interface GetRoomsByFloorUseCase {

    suspend operator fun invoke(floor: String): RequestResult<List<RoomData>>
}