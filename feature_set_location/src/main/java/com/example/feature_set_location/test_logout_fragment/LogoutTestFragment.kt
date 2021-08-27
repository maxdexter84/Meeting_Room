package com.example.feature_set_location.test_logout_fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.example.core_module.user_logout.LogoutUser
import com.example.feature_set_location.R
import com.example.feature_set_location.databinding.LogoutTestLayoutBinding
import java.util.*
import javax.inject.Inject

class LogoutTestFragment : Fragment(R.layout.logout_test_layout) {

    private lateinit var binding: LogoutTestLayoutBinding
    @Inject
    lateinit var logoutUser: LogoutUser

    override fun onAttach(context: Context) {
        super.onAttach(context)

        DaggerLogoutComponent.builder()
            .logoutModule(LogoutModule())
            .sharedPreferencesModule(SharedPreferencesModule(requireContext()))
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LogoutTestLayoutBinding.bind(view)

        with(binding) {
            buttonLogout.setOnClickListener { logoutUser.logout() }
            buttonCheckTimeLogout.setOnClickListener { checkUserTokenExpired() }
        }
    }

    private fun checkUserTokenExpired() {
        if (logoutUser.deleteExpiredToken()) {

        }
    }
}