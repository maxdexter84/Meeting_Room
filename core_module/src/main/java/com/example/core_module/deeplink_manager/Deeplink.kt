package com.example.core_module.deeplink_manager

import android.net.Uri
import androidx.navigation.NavController

class Deeplink(private val controller: NavController) {

    companion object{
        val GO_TO_LANDING_SCREEN: Uri
            get() = parseStringToUri("ui://landingScreenFragment")
        val GO_TO_LOGIN_SCREEN: Uri
            get() = parseStringToUri("feature_login://loginFragment")
        val GO_TO_MY_SPACE: Uri
            get() = parseStringToUri("feature_landing_screen://landingFragment")
        val GO_TO_ROOMS: Uri
            get() = parseStringToUri("feature_rooms_screen://roomsEventGridFragment")
        val GO_TO_SET_LOCATION: Uri
            get() = parseStringToUri("feature_set_location://locationFragment")
        val GO_TO_BOTTOM_NAV: Uri
            get() = parseStringToUri("app://bottom_navigation")
        private fun parseStringToUri(deeplink: String): Uri {
            return Uri.parse(deeplink)
        }

    }


    fun navigate(deeplink: Uri) {
        controller.navigate(deeplink)
    }

}