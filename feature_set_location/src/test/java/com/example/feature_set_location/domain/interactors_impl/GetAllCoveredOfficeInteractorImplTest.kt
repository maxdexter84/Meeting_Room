package com.example.feature_set_location.domain.interactors_impl

import com.example.core_network.RequestResult
import com.example.feature_set_location.data.remote.dto.Office
import com.example.feature_set_location.domain.repository.LocationRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

class GetAllCoveredOfficeInteractorImplTest {

    private val repositoryImpl = Mockito.mock(LocationRepository::class.java)
    private val getAllCovered = GetAllCoveredOfficeInteractorImpl(repositoryImpl)
    private val office = Office(CITY, COUNTRY, 1, listOf(EMPTY_STRING))
    private val listOffice = listOf(office)

    @Test
    fun getAllCoveredReturnRequestResultListString() {
        val requestResultListOffice = RequestResult.Success(listOffice)
        Mockito.`when`(runBlocking { repositoryImpl.getAllOffice() })
            .thenReturn(requestResultListOffice)
        val actual = runBlocking {
            getAllCovered.getData()
        }
        val expected = RequestResult.Success(listOf(CITY))
        assertEquals(expected, actual)
    }

    @Test
    fun getAllCoveredReturnRequestResultError() {
        val requestResultError = RequestResult.Error(ERROR, ERROR_ID)
        Mockito.`when`(runBlocking { repositoryImpl.getAllOffice() }).thenReturn(requestResultError)
        val actual = runBlocking {
            getAllCovered.getData()
        }
        assertEquals(requestResultError, actual)
    }

    @Test
    fun getAllCoveredReturnRequestResultLoading() {
        val requestResultLoading = RequestResult.Loading
        Mockito.`when`(runBlocking { repositoryImpl.getAllOffice() })
            .thenReturn(requestResultLoading)
        val actual = runBlocking {
            getAllCovered.getData()
        }
        assertEquals(requestResultLoading, actual)
    }
}