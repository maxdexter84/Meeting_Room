package com.example.core_network

import com.example.core_network.location_interfaces.LocationInterface
import com.example.core_network.location_posts.CountryPost
import com.example.core_network.location_responses.City
import com.example.core_network.location_responses.Country
import com.example.core_network.user_interfaces.LogInInterface
import com.example.core_network.user_posts.LogInRequest
import com.example.core_network.user_responses.LogInResponse
import okhttp3.ResponseBody
import retrofit2.Response

class GagForInternetsRequests : LogInInterface, LocationInterface {
    override suspend fun logInUser(post: LogInRequest): Response<LogInResponse> {
        return if (arrayOfUsers.filter { it.username == post.username }.count() == 1) {
            Response.success(arrayOfUsers.filter { it.username == post.username }[0])
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
                    roles.subList(0, 1),
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
            CountryPost("Ukraine"),
            CountryPost("Russia"),
            CountryPost("Belarus"),
        )
        private val countriesOfUkraine = arrayListOf(
            City("Dnipro"),
            City("Odessa"),
            City("Kyiv")
        )
        private val countriesOfRussia = arrayListOf(
            City("Moscow"),
            City("Piter"),
            City("Kazan"),
            City("Vladivostok")
        )
        private val countriesOfBelarus = arrayListOf(
            City("Minsk"),
            City("Gomel"),
        )

    }

    override suspend fun getAllAvailableCountries(): Response<List<CountryPost>> {
        return Response.success(
            countries
        )
    }

    override suspend fun getAllAvailableCities(post: CountryPost): Response<List<City>> {
        if (countries.contains(post)) return Response.success(getAllCitiesFromCountry(post))
        return Response.error(404, null)
    }

    private fun getAllCitiesFromCountry(post: CountryPost): List<City> {
        return when (post.name) {
            "Ukraine" -> countriesOfUkraine
            "Russia" -> countriesOfRussia
            "Belarus" -> countriesOfBelarus
            else -> arrayListOf(City("none"), City("none"), City("none"))
        }

    }
}