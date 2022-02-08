package com.example.feature_set_location.domain.interactors_impl

import com.example.core_network.RequestResult
import com.example.feature_set_location.data.remote.dto.MyOffice
import com.example.feature_set_location.domain.interactors.GetUserOfficeCityInteractor
import com.example.feature_set_location.domain.repository.LocationRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class GetUserOfficeCityInteractorImplTest {

    private val repository = mock(LocationRepository::class.java)
    private val myOffice = MyOffice(EMPTY_STRING, USER_ID,EMPTY_STRING, CITY, COUNTRY,EMPTY_STRING, ROLE,EMPTY_STRING,EMPTY_STRING,EMPTY_STRING, OFFICE_ID)
    private val getUserOffice = GetUserOfficeCityInteractorImpl(repository)

    @Test
    fun getDataReturnRequestResultString() {
        Mockito.`when`(runBlocking {
            repository.getUserOffice()
        }).thenReturn(RequestResult.Success(myOffice))

        val actual = runBlocking {
            getUserOffice.getData()
        }
        val expected = RequestResult.Success(CITY)
        assertEquals(expected, actual)
    }

    @Test
    fun getRoleReturnRequestResultString() {
        Mockito.`when`(runBlocking {
            repository.getUserOffice()
        }).thenReturn(RequestResult.Success(myOffice))
        val actual = runBlocking {
            getUserOffice.getRole()
        }
        val expected = RequestResult.Success(ROLE)
        assertEquals(expected, actual)
    }

    @Test
    fun getUserIdReturnRequestResultInt() {
        Mockito.`when`(runBlocking {
            repository.getUserOffice()
        }).thenReturn(RequestResult.Success(myOffice))

        val actual = runBlocking {
            getUserOffice.getUserId()
        }
        val expected = RequestResult.Success(USER_ID)
        assertEquals(expected, actual)
    }

    @Test
    fun getOfficeId() {
        Mockito.`when`(runBlocking {
            repository.getUserOffice()
        }).thenReturn(RequestResult.Success(myOffice))
        val actual = runBlocking {
            getUserOffice.getOfficeId()
        }
        val expected = RequestResult.Success(OFFICE_ID)
        assertEquals(expected, actual)
    }
}