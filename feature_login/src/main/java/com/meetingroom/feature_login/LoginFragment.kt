package com.meetingroom.feature_login

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view.login_button.MainActionButtonState
import com.meetingroom.feature_login.databinding.LoginFragmentBinding
import com.meetingroom.feature_login.di.DaggerLoginComponent
import com.meetingroom.feature_login.di.LoginFragmentModule
import javax.inject.Inject

class LoginFragment : BaseFragment<LoginFragmentBinding>(LoginFragmentBinding::inflate) {

    @Inject
    lateinit var viewModel: LoginFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerLoginComponent.builder()
            .loginFragmentModule(LoginFragmentModule(this))
            .sharedPreferencesModule(SharedPreferencesModule(requireContext()))
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.requestResult.observe(viewLifecycleOwner, {
            binding.logInButtonMainActivity.state = MainActionButtonState.ENABLED
            findNavController().navigate(R.id.action_login_fragment_to_nav_between_locations_fragment)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, {
            binding.editEmailLoginFragment.textError = requireContext().getString(it)
            binding.logInButtonMainActivity.state = MainActionButtonState.ENABLED
        })

        binding.logInButtonMainActivity.setOnClickListener {
            viewModel.tryToLogIn(
                binding.editEmailLoginFragment.text!!,
                binding.editPasswordLoginFragment.text!!
            )
            binding.logInButtonMainActivity.state = MainActionButtonState.LOADING
        }
    }
}