package com.example.feature_set_location.domain.interactors_impl

import com.example.core_network.RequestResult
import com.example.feature_set_location.data.remote.dto.UserUpdateOffice
import com.example.feature_set_location.domain.repository.LocationRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times

class SaveOfficeIdInteractorImplTest {

    private val repository = mock(LocationRepository::class.java)
    private val saveOfficeId = SaveOfficeIdInteractorImpl(repository)
    private val userOfficeId = UserUpdateOffice(OFFICE_ID)

    @Test
    fun saveOfficeIdReturnUnit() {
        Mockito.`when`(runBlocking {
            repository.saveOfficeId(userOfficeId)
        }).thenReturn(RequestResult.Success(Unit))
        val actual = runBlocking {
            saveOfficeId.saveOfficeId(OFFICE_ID)
        }
        val expected = Unit
        runBlocking {
            Mockito.verify(repository, times(1)).saveOfficeId(userOfficeId)
        }
        assertEquals(expected, actual)
    }
}