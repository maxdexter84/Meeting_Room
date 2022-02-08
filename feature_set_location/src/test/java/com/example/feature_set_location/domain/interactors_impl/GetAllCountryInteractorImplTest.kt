package com.example.feature_set_location.domain.interactors_impl

import com.example.core_network.RequestResult
import com.example.feature_set_location.data.remote.dto.Office
import com.example.feature_set_location.domain.model.OfficeOfSelectedCountry
import com.example.feature_set_location.domain.repository.LocationRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test
import org.mockito.Mockito

class GetAllCountryInteractorImplTest {

    private val repositoryImpl = Mockito.mock(LocationRepository::class.java)
    private val getAllCountry = GetAllCountryInteractorImpl(repositoryImpl)
    private val office = Office(EMPTY_STRING, COUNTRY, 1, listOf(EMPTY_STRING))
    private val listOffice = listOf(office)

    @Test
    fun getAllCountryReturnRequestResultListString() {
        val requestResultListOffice = RequestResult.Success(listOffice)
        Mockito.`when`(runBlocking { repositoryImpl.getAllOffice() })
            .thenReturn(requestResultListOffice)
        val actual = runBlocking {
            getAllCountry.getData()
        }
        val expected = RequestResult.Success(listOf(COUNTRY))
        assertEquals(expected, actual)
    }

    @Test
    fun getAllCountryReturnRequestResultError() {
        val requestResultError = RequestResult.Error(ERROR, ERROR_ID)
        Mockito.`when`(runBlocking { repositoryImpl.getAllOffice() }).thenReturn(requestResultError)
        val actual = runBlocking {
            getAllCountry.getData()
        }
        assertEquals(requestResultError, actual)
    }

    @Test
    fun getAllCountryReturnRequestResultLoading() {
        val requestResultLoading = RequestResult.Loading
        Mockito.`when`(runBlocking { repositoryImpl.getAllOffice() }).thenReturn(requestResultLoading)
        val actual = runBlocking {
            getAllCountry.getData()
        }
        assertEquals(requestResultLoading, actual)
    }
}