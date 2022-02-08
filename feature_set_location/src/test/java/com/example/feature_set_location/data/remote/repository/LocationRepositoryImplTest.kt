package com.example.feature_set_location.data.remote.repository

import com.example.core_network.RequestMaker
import com.example.core_network.RequestResult
import com.example.feature_set_location.data.remote.dto.MyOffice
import com.example.feature_set_location.data.remote.dto.Office
import com.example.feature_set_location.data.remote.dto.UserUpdateOffice
import com.example.feature_set_location.data.remote.location_api.LocationApi
import com.example.feature_set_location.domain.interactors_impl.EMPTY_STRING
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import retrofit2.Response

class LocationRepositoryImplTest {

    private val requestMaker = RequestMaker()
    private val api = mock(LocationApi::class.java)
    private val myOffice = MyOffice(EMPTY_STRING, 1, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, 1)
    private val listOffice = listOf(Office(EMPTY_STRING, EMPTY_STRING, 1, listOf(EMPTY_STRING)))
    private val requestResult = RequestResult.Success(myOffice)
    private val requestResultList = RequestResult.Success(listOffice)
    private val requestResultUnit = RequestResult.Success(Unit)
    private val locationRepositoryImplTest = LocationRepositoryImpl(requestMaker, api)
    private val userOfficeId = mock(UserUpdateOffice::class.java)

    @Test
    fun testGetUserOfficeReturnRequestResult() {
        Mockito.`when`(runBlocking {
            requestMaker.safeApiCall {
                api.getMyOffice()
            }
        }).thenReturn(requestResult)
        Mockito.`when`(runBlocking {
            api.getMyOffice()
        }).thenReturn(Response.success(myOffice))
        val actual = runBlocking {
            locationRepositoryImplTest.getUserOffice()
        }
        val expected = requestResult
        assertEquals(expected, actual)
        runBlocking {
            Mockito.verify(api, Mockito.atLeastOnce()).getMyOffice()
        }
    }

    @Test
    fun testGetAllOfficeReturnRequestResult() {
        Mockito.`when`(runBlocking {
            requestMaker.safeApiCall {
                api.getAllOffice()
            }
        }).thenReturn(requestResultList)
        Mockito.`when`(runBlocking {
            api.getAllOffice()
        }).thenReturn(Response.success(listOffice))
        val actual = runBlocking {
            locationRepositoryImplTest.getAllOffice()
        }
        val expected = requestResultList
        assertEquals(expected, actual)
        runBlocking {
            Mockito.verify(api, Mockito.atLeastOnce()).getAllOffice()
        }
    }

    @Test
    fun checkSaveOfficeId(){
        Mockito.`when`(runBlocking {
            requestMaker.safeApiCall {
                api.saveOfficeId(userOfficeId)
            }
        }).thenReturn(requestResultUnit)
        Mockito.`when`(runBlocking {
            api.saveOfficeId(userOfficeId)
        }).thenReturn(Response.success(Unit))
        val actual = runBlocking {
            locationRepositoryImplTest.saveOfficeId(userOfficeId)
        }
        val expected = requestResultUnit
        assertEquals(expected, actual)
        runBlocking {
            Mockito.verify(api, Mockito.atLeastOnce()).saveOfficeId(userOfficeId)
        }
    }
}