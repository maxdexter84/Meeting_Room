package com.meetingroom.feature_workplaces_screen.domain.use_case_impl

import com.example.core_network.RequestResult
import com.meetingroom.feature_workplaces_screen.domain.model.RoomData
import com.meetingroom.feature_workplaces_screen.domain.use_case.GetRoomsByFloorUseCase
import javax.inject.Inject

class GetRoomsByFloorUseCaseImpl @Inject constructor() : GetRoomsByFloorUseCase {

    override suspend operator fun invoke(floor: String): RequestResult<List<RoomData>> =
        when (floor) {
            FLOOR_1 -> {
                RequestResult.Success(
                    listOf(
                        RoomData(DEPARTMENT_1_FLOOR_1, NUMBER_OF_PLACES_1),
                        RoomData(DEPARTMENT_2_FLOOR_1, NUMBER_OF_PLACES_2)
                    )
                )
            }
            FLOOR_2 -> {
                RequestResult.Success(
                    listOf(
                        RoomData(DEPARTMENT_1_FLOOR_2, NUMBER_OF_PLACES_1),
                        RoomData(DEPARTMENT_2_FLOOR_2, NUMBER_OF_PLACES_2)
                    )
                )
            }
            else -> {
                RequestResult.Success(
                    listOf(
                        RoomData(DEFAULT_VALUE, NUMBER_OF_PLACES_1),
                        RoomData(DEFAULT_VALUE, NUMBER_OF_PLACES_2)
                    )
                )
            }
        }


    companion object {
        const val FLOOR_1 = "First"
        const val FLOOR_2 = "Second"
        const val DEPARTMENT_1_FLOOR_1 = "Java department first"
        const val DEPARTMENT_2_FLOOR_1 = "Kotlin department first"
        const val DEPARTMENT_1_FLOOR_2 = "Java department sec"
        const val DEPARTMENT_2_FLOOR_2 = "Kotlin department sec"
        const val NUMBER_OF_PLACES_1 = 5
        const val NUMBER_OF_PLACES_2 = 10
        const val DEFAULT_VALUE = "Default"
    }
}