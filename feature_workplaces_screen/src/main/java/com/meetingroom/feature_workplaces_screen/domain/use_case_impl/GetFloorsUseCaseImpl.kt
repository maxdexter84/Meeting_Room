package com.meetingroom.feature_workplaces_screen.domain.use_case_impl

import com.example.core_network.RequestResult
import com.meetingroom.feature_workplaces_screen.domain.model.OfficeData
import com.meetingroom.feature_workplaces_screen.domain.use_case.GetFloorsUseCase
import javax.inject.Inject

class GetFloorsUseCaseImpl @Inject constructor() : GetFloorsUseCase {

    override suspend operator fun invoke(): RequestResult<List<OfficeData>> =
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