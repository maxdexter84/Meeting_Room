package com.example.feature_set_location.domain.interactors_impl

import com.example.core_network.RequestResult
import com.example.feature_set_location.data.remote.dto.Office
import com.example.feature_set_location.domain.model.OfficeOfSelectedCountry
import com.example.feature_set_location.domain.repository.LocationRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class GetAllCityInCountryInteractorImplTest {

    private val repositoryImpl = mock(LocationRepository::class.java)
    private val getAllCityInCountry = GetAllCityInCountryInteractorImpl(repositoryImpl)
    private val office = Office(EMPTY_STRING, COUNTRY, 1, listOf(EMPTY_STRING))
    private val officeOfSelectedCountry = OfficeOfSelectedCountry(EMPTY_STRING, 1)
    private val listOffice = listOf(office)
    private val listOfficeOfSelectedCountry = listOf(officeOfSelectedCountry)

    @Test
    fun getAllCityInCountryReturnRequestResultListOffice() {
        val requestResultListOffice = RequestResult.Success(listOffice)
        Mockito.`when`(runBlocking { repositoryImpl.getAllOffice() })
            .thenReturn(requestResultListOffice)

        val actual = runBlocking {
            getAllCityInCountry.getData(COUNTRY)
        }
        val expected = RequestResult.Success(listOfficeOfSelectedCountry)

        assertEquals(expected, actual)
    }

    @Test
    fun getAllCityInCountryReturnRequestResultError() {
        val requestResultError = RequestResult.Error(ERROR, ERROR_ID)
        Mockito.`when`(runBlocking { repositoryImpl.getAllOffice() }).thenReturn(requestResultError)

        val actual = runBlocking {
            getAllCityInCountry.getData(COUNTRY)
        }
        assertEquals(requestResultError, actual)
    }

    @Test
    fun getAllCityInCountryReturnRequestResultLoading() {
        val requestResultError = RequestResult.Loading
        Mockito.`when`(runBlocking { repositoryImpl.getAllOffice() }).thenReturn(requestResultError)

        val actual = runBlocking {
            getAllCityInCountry.getData(COUNTRY)
        }
        assertEquals(requestResultError, actual)
    }
}