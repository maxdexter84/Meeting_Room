package com.example.core_module.deeplink_manager

import android.net.Uri
import androidx.navigation.NavController

class DeeplinkNavigatorHelper(private val controller: NavController) {

    companion object{
        const val GO_TO_LANDING_SCREEN = "ui://landingScreenFragment"
        const val GO_TO_LOGIN_SCREEN = "feature_login://loginFragment"
        const val GO_TO_MY_SPACE = "feature_landing_screen://landingFragment"
        const val GO_TO_ROOMS = "feature_rooms_screen://roomsEventGridFragment"
        const val GO_TO_SET_LOCATION = "feature_set_location://locationFragment"
        const val GO_TO_BOTTOM_NAV = "app://bottom_navigation"
    }

    fun navigate(deeplink: String) {
        val uri = when(deeplink){
            GO_TO_LANDING_SCREEN -> parseStringToUri(deeplink)
            GO_TO_LOGIN_SCREEN -> parseStringToUri(deeplink)
            GO_TO_MY_SPACE -> parseStringToUri(deeplink)
            GO_TO_ROOMS -> parseStringToUri(deeplink)
            GO_TO_SET_LOCATION -> parseStringToUri(deeplink)
            GO_TO_BOTTOM_NAV -> parseStringToUri(deeplink)
            else-> throw Exception("Wrong constant")
        }
        controller.navigate(uri)
    }

    private fun parseStringToUri(deeplink: String): Uri {
        return Uri.parse(deeplink)
    }
}