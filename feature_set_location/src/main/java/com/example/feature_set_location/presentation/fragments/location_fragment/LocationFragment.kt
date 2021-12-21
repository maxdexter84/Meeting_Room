package com.example.feature_set_location.presentation.fragments.location_fragment

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.core_module.component_manager.IHasComponent
import com.example.core_module.component_manager.XInjectionManager
import com.example.feature_set_location.R
import com.example.feature_set_location.databinding.LocationFragmentBinding
import com.example.feature_set_location.di.DaggerSetLocationComponent
import com.example.feature_set_location.di.SetLocationComponent
import com.meeringroom.ui.view.base_classes.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class LocationFragment : BaseFragment<LocationFragmentBinding>(LocationFragmentBinding::inflate),
    IHasComponent<SetLocationComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.bindComponent(this).inject(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: LocationFragmentViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivSelectLayoutLocationFragment.setOnClickListener {
            findNavController().navigate(LocationFragmentDirections.actionLocationFragmentToCountryFragment())
        }

        binding.btnConfirmLocationFragment.setOnClickListener {
            navigateToDeepLink(resources.getString(R.string.deeplink_uri_bottom_navigation))
        }
        observeData()
        observeCovered()
    }

    private fun observeCovered() {
        viewModel.covered.onEach {
            when (it) {
                true -> binding.tvErrorTextLocationFragment.visibility = View.INVISIBLE
                false -> binding.tvErrorTextLocationFragment.visibility = View.VISIBLE
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeData() {
        viewModel.myOffice.onEach {
            binding.tvCitySelectText.text = it
        }.launchIn(lifecycleScope)
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