package com.meetingroom.feature_workplaces_screen.domain.use_case

import com.example.core_network.RequestResult
import com.meetingroom.feature_workplaces_screen.domain.model.OfficeData

interface GetFloorsUseCase {

    suspend operator fun invoke(): RequestResult<List<OfficeData>>
}