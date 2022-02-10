package com.meetingroom.feature_workplaces_screen.domain.use_case

import com.example.core_network.RequestResult
import com.meetingroom.feature_workplaces_screen.domain.model.OfficeData

class GetOfficeDataUseCase {

    suspend operator fun invoke(): RequestResult<List<OfficeData>> =
        RequestResult.Success(
            listOf(
                OfficeData(MOCK_DATA_1, MOCK_DATA_2),
                OfficeData(MOCK_DATA_3, MOCK_DATA_4)
            )
        )

    companion object {
        const val MOCK_DATA_1 = "First"
        const val MOCK_DATA_2 = 25
        const val MOCK_DATA_3 = "Second"
        const val MOCK_DATA_4 = 30
    }
}