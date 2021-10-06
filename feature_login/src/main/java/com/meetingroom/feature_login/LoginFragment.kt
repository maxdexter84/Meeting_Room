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
        with(binding) {
            viewModel.requestResult.observe(viewLifecycleOwner, {
                logInButtonMainActivity.state = MainActionButtonState.ENABLED
                findNavController().navigate(R.id.action_login_fragment_to_nav_between_locations_fragment)
            })

            viewModel.errorMessage.observe(viewLifecycleOwner, {
                editEmailLoginFragment.textError = requireContext().getString(it)
                logInButtonMainActivity.state = MainActionButtonState.DISABLED
            })

            editEmailLoginFragment.afterTextChangeAction = onTextChangeListener
            editPasswordLoginFragment.afterTextChangeAction = onTextChangeListener
            logInButtonMainActivity.state = MainActionButtonState.DISABLED
            logInButtonMainActivity.setOnClickListener {
                viewModel.tryToLogIn(
                    editEmailLoginFragment.text!!,
                    editPasswordLoginFragment.text!!
                )
                logInButtonMainActivity.state = MainActionButtonState.LOADING
            }
        }
    }

    private val onTextChangeListener = {
        with(binding) {
            editEmailLoginFragment.textError = ""
            editPasswordLoginFragment.textError = ""
            if (editEmailLoginFragment.text!!.isNotBlank() && editPasswordLoginFragment.text!!.isNotBlank()) {
                logInButtonMainActivity.state = MainActionButtonState.ENABLED
            } else {
                logInButtonMainActivity.state = MainActionButtonState.DISABLED
            }
        }
    }
}