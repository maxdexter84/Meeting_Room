package com.example.feature_set_location.di

import com.example.feature_set_location.domain.interactors.GetAllCityInCountryInteractor
import com.example.feature_set_location.domain.interactors.GetAllCountryInteractor
import com.example.feature_set_location.domain.interactors.GetAllCoveredOfficeInteractor
import com.example.feature_set_location.domain.interactors.GetUserOfficeCityInteractor
import com.example.feature_set_location.domain.interactors_impl.GetAllCityInCountryInteractorImpl
import com.example.feature_set_location.domain.interactors_impl.GetAllCountryInteractorImpl
import com.example.feature_set_location.domain.interactors_impl.GetAllCoveredOfficeInteractorImpl
import com.example.feature_set_location.domain.interactors_impl.GetUserOfficeCityInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface InteractorModule {

    @Binds
    fun bindsGetUserOfficeInteractor(interactor: GetUserOfficeCityInteractorImpl): GetUserOfficeCityInteractor

    @Binds
    fun bindsGetAllCountryInteractor(interactor: GetAllCountryInteractorImpl): GetAllCountryInteractor

    @Binds
    fun bindsGetAllCityInCountryInteractor(interactor: GetAllCityInCountryInteractorImpl): GetAllCityInCountryInteractor

    @Binds
    fun bindsGetAllCoveredOfficeInteractor(interactor: GetAllCoveredOfficeInteractorImpl): GetAllCoveredOfficeInteractor

}