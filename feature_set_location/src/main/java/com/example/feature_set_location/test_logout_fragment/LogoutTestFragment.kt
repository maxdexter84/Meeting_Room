package com.example.feature_set_location.test_logout_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.example.core_module.user_logout.LogoutUser
import com.example.feature_set_location.R
import com.example.feature_set_location.databinding.LogoutTestLayoutBinding
import javax.inject.Inject

class LogoutTestFragment : Fragment(R.layout.logout_test_layout) {

    private lateinit var binding: LogoutTestLayoutBinding
    private lateinit var logoutUser: LogoutUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LogoutTestLayoutBinding.bind(view)

        logoutUser = LogoutUser()

        with(binding) {
            buttonLogout.setOnClickListener { logoutCurrentUser() }
            buttonCheckTimeLogout.setOnClickListener { doTimeStuff() }
        }
    }

    private fun logoutCurrentUser() {

    }

    private fun doTimeStuff() {
    }
}