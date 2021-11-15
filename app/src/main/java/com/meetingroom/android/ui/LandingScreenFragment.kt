package com.meetingroom.android.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meetingroom.android.R
import com.meetingroom.android.databinding.FragmentLandingScreenBinding

class LandingScreenFragment: BaseFragment<FragmentLandingScreenBinding>(FragmentLandingScreenBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = (childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        binding.bottomNavView.mainBottomNavigationView.setupWithNavController(navController)
    }

}
