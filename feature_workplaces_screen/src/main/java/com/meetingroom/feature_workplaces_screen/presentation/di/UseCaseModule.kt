package com.meetingroom.feature_workplaces_screen.presentation.di

import com.meetingroom.feature_workplaces_screen.domain.use_case.GetFloorsUseCase
import com.meetingroom.feature_workplaces_screen.domain.use_case.GetRoomsByFloorUseCase
import com.meetingroom.feature_workplaces_screen.domain.use_case_impl.GetFloorsUseCaseImpl
import com.meetingroom.feature_workplaces_screen.domain.use_case_impl.GetRoomsByFloorUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCaseModule {

    @Binds
    fun bindsGetFloorsUseCase(useCase: GetFloorsUseCaseImpl): GetFloorsUseCase

    @Binds
    fun bindsGetRoomsByFloorUseCase(useCase: GetRoomsByFloorUseCaseImpl): GetRoomsByFloorUseCase
}