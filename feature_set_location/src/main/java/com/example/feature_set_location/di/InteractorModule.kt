package com.example.feature_set_location.di

import com.example.feature_set_location.domain.interactors.*
import com.example.feature_set_location.domain.interactors_impl.*
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

    @Binds
    fun bindsSaveOfficeIdInteractor(interactor: SaveOfficeIdInteractorImpl): SaveOfficeIdInteractor

}