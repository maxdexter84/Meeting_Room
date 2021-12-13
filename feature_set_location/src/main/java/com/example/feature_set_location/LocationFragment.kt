package com.example.feature_set_location

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.core_module.component_manager.IHasComponent
import com.example.core_module.component_manager.XInjectionManager
import com.example.feature_set_location.databinding.LocationFragmentBinding
import com.example.feature_set_location.di.DaggerSetLocationComponent
import com.example.feature_set_location.di.SetLocationComponent
import com.meeringroom.ui.view.base_classes.BaseFragment

class LocationFragment : BaseFragment<LocationFragmentBinding>(LocationFragmentBinding::inflate), IHasComponent<SetLocationComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.bindComponent(this).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvCitySelectText.text = arguments?.getString(CITY_KEY) ?: ""

        binding.ivSelectLayoutLocationFragment.setOnClickListener {
            findNavController().navigate(R.id.action_locationFragment_to_countryFragment)
        }

        binding.btnConfirmLocationFragment.setOnClickListener {
            navigateToDeepLink(resources.getString(R.string.deeplink_uri_bottom_navigation))
        }
    }

    companion object {
        const val CITY_KEY = "CITY_KEY"
    }

    private fun navigateToDeepLink(link: String) {
        val uri = Uri.parse(link)
        findNavController().navigate(uri)
    }

    override fun getComponent(): SetLocationComponent {
        return DaggerSetLocationComponent
            .factory()
            .create(requireContext(), XInjectionManager.findComponent())
    }
}