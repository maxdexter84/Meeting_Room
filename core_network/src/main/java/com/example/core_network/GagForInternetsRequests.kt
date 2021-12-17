package com.example.core_network

import com.example.core_network.location_interfaces.LocationInterface
import com.example.core_network.location_posts.GetAllAvailableCitiesRequest
import com.example.core_network.location_responses.GetAllAvailableCitiesResponse
import com.example.core_network.location_responses.GetAllAvailableCountriesResponse

class GagForInternetsRequests : LocationInterface {
    companion object {
        private val typeOfTokens = listOf("usual token", "another token")
        private val roles = listOf("normal user", "admin")
        private val names =
            listOf("sascha", "nikita", "vladimir", "andrey", "qwerty", "v.khrytanenka")
        private val countries = arrayListOf(
            GetAllAvailableCitiesRequest("Ukraine"),
            GetAllAvailableCitiesRequest("Russia"),
            GetAllAvailableCitiesRequest("Belarus"),
        )
        private val countriesResponse = arrayListOf(
            GetAllAvailableCountriesResponse("Ukraine"),
            GetAllAvailableCountriesResponse("Russia"),
            GetAllAvailableCountriesResponse("Belarus"),
        )
        private val citiesOfUkraine = arrayListOf(
            GetAllAvailableCitiesResponse("Kyiv"),
            GetAllAvailableCitiesResponse("Odessa"),
            GetAllAvailableCitiesResponse("Dnipro"),
            GetAllAvailableCitiesResponse("Kharkiv")
        )
        private val citiesOfRussia = arrayListOf(
            GetAllAvailableCitiesResponse("Moscow"),
            GetAllAvailableCitiesResponse("Saint Petersburg"),
            GetAllAvailableCitiesResponse("Kazan"),
            GetAllAvailableCitiesResponse("Vladivostok")
        )
        private val citiesOfBelarus = arrayListOf(
            GetAllAvailableCitiesResponse("Minsk"),
            GetAllAvailableCitiesResponse("Gomel"),
            GetAllAvailableCitiesResponse("Polotsk"),
            GetAllAvailableCitiesResponse("Vitebsk"),
        )

    }

    override suspend fun getAllAvailableCountries(): RequestResult<List<GetAllAvailableCountriesResponse>> {
        return RequestResult.Success(
            countriesResponse
        )
    }

    override suspend fun getAllAvailableCities(post: GetAllAvailableCitiesRequest): RequestResult<List<GetAllAvailableCitiesResponse>> {
        if (countries.contains(post)) return RequestResult.Success(getAllCitiesFromCountry(post))
        return RequestResult.Error("No cities", 404)
    }

    private fun getAllCitiesFromCountry(post: GetAllAvailableCitiesRequest): List<GetAllAvailableCitiesResponse> {
        return when (post.name) {
            "Ukraine" -> citiesOfUkraine
            "Russia" -> citiesOfRussia
            "Belarus" -> citiesOfBelarus
            else -> arrayListOf(
                GetAllAvailableCitiesResponse("none"),
                GetAllAvailableCitiesResponse("none"),
                GetAllAvailableCitiesResponse("none")
            )
        }

    }
}