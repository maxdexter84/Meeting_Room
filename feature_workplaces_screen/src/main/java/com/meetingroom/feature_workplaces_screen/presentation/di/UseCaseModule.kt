package com.meetingroom.feature_workplaces_screen.presentation.di

import com.meetingroom.feature_workplaces_screen.domain.use_case.GetOfficeDataUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun bindsGetOfficeDataUseCase(): GetOfficeDataUseCase = GetOfficeDataUseCase()
}