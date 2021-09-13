package com.example.core_network

import com.example.core_network.location_interfaces.LocationInterface
import com.example.core_network.location_posts.GetAllAvailableCitiesRequest
import com.example.core_network.location_responses.GetAllAvailableCitiesResponse
import com.example.core_network.location_responses.GetAllAvailableCountriesResponse
import com.example.core_network.user_interfaces.LogInInterface
import com.example.core_network.user_posts.LogInRequest
import com.example.core_network.user_responses.LogInResponse
import okhttp3.ResponseBody
import retrofit2.Response

class GagForInternetsRequests : LogInInterface, LocationInterface {
    override suspend fun logInUser(post: LogInRequest): Response<LogInResponse> {
        return if (arrayOfUsers.filter { it.email == post.email }.count() == 1) {
            Response.success(arrayOfUsers.filter { it.email == post.email }[0])
        } else Response.error(404, ResponseBody.Companion.create(null, "Something wrong"))
    }

    companion object {
        private val arrayOfUsers by lazy {
            arrayListOf(
                LogInResponse(
                    10,
                    names[1],
                    names[1] + andersenEnding,
                    roles.subList(0, 1),
                    typeOfTokens[0],
                    "wjfwfwf4wgw6g46wg6swopkgsvs121vw4DGs6g4sw"
                ),
                LogInResponse(
                    33,
                    names[0],
                    names[0] + andersenEnding,
                    roles.subList(1, roles.size),
                    typeOfTokens[0],
                    ":QWf563fqCvwq56f5234f6561qfqF98f56a"
                ),
                LogInResponse(
                    5,
                    names[2],
                    names[2] + andersenEnding,
                    roles.subList(0, 1),
                    typeOfTokens[0],
                    "tfuwhkncmcF??FSFs252sgsWwf365fzdwds"
                ),
                LogInResponse(
                    1,
                    "qwerty",
                    "qwerty$andersenEnding",
                    roles,
                    "usual",
                    "gYqefdszxfg78chv4bjok8954985rdtyfwqyg5Tavhac"
                )

            )
        }
        private const val andersenEnding: String = "@andersenlab.com"
        private val typeOfTokens = listOf("usual token", "another token")
        private val roles = listOf("normal user", "admin")
        private val names = listOf("sascha", "nikita", "vladimir", "andrey", "qwerty")
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
            GetAllAvailableCitiesResponse("Dnipro"),
            GetAllAvailableCitiesResponse("Odessa"),
            GetAllAvailableCitiesResponse("Kyiv")
        )
        private val citiesOfRussia = arrayListOf(
            GetAllAvailableCitiesResponse("Moscow"),
            GetAllAvailableCitiesResponse("Piter"),
            GetAllAvailableCitiesResponse("Kazan"),
            GetAllAvailableCitiesResponse("Vladivostok")
        )
        private val citiesOfBelarus = arrayListOf(
            GetAllAvailableCitiesResponse("Minsk"),
            GetAllAvailableCitiesResponse("Gomel"),
        )

    }

    override suspend fun getAllAvailableCountries(): Response<List<GetAllAvailableCountriesResponse>> {
        return Response.success(
            countriesResponse
        )
    }

    override suspend fun getAllAvailableCities(post: GetAllAvailableCitiesRequest): Response<List<GetAllAvailableCitiesResponse>> {
        if (countries.contains(post)) return Response.success(getAllCitiesFromCountry(post))
        return Response.error(404, null)
    }

    private fun getAllCitiesFromCountry(post: GetAllAvailableCitiesRequest): List<GetAllAvailableCitiesResponse> {
        return when (post.name) {
            "Ukraine" -> citiesOfUkraine
            "Russia" -> citiesOfRussia
            "Belarus" -> citiesOfBelarus
            else -> arrayListOf(GetAllAvailableCitiesResponse("none"), GetAllAvailableCitiesResponse("none"), GetAllAvailableCitiesResponse("none"))
        }

    }
}