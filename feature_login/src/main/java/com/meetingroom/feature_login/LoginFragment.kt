package com.meetingroom.feature_login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meeringroom.ui.view.base_fragment.BaseFragment
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

            editEmailLoginFragment.addTextChangeListener(onTextChangeListener)
            editPasswordLoginFragment.addTextChangeListener(onTextChangeListener)
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

    private val onTextChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            with(binding) {
                if (editEmailLoginFragment.text!!.isNotBlank() && editPasswordLoginFragment.text!!.isNotBlank()) {
                    logInButtonMainActivity.state = MainActionButtonState.ENABLED
                } else {
                    logInButtonMainActivity.state = MainActionButtonState.DISABLED
                }
            }
        }}
}