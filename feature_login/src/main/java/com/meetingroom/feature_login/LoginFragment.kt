package com.meetingroom.feature_login

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view.login_button.MainActionButtonState
import com.meetingroom.feature_login.databinding.LoginFragmentBinding
import com.meetingroom.feature_login.di.DaggerLoginComponent
import com.meetingroom.feature_login.di.LoginComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import javax.inject.Inject

class LoginFragment : BaseFragment<LoginFragmentBinding>(LoginFragmentBinding::inflate), IHasComponent<LoginComponent> {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: LoginFragmentViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.bindComponent(this).inject(this)
    }

    override fun getComponent(): LoginComponent {
        return DaggerLoginComponent
            .factory()
            .create(requireContext(), XInjectionManager.findComponent())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel.requestResult.observe(viewLifecycleOwner, {
                logInButtonMainActivity.state = MainActionButtonState.LOADING
                lifecycleScope.launch {
                    delay(1000)
                    val uri = Uri.parse(resources.getString(com.meetingroom.ui.R.string.deeplink_uri_set_locations_screen))
                    findNavController().navigate(uri)
                }

            })

            viewModel.errorMessage.observe(viewLifecycleOwner, {
                with(requireContext().getString(it)) {
                    editEmailLoginFragment.textError = this
                    editPasswordLoginFragment.textError = this
                }
                logInButtonMainActivity.state = MainActionButtonState.DISABLED
            })

            editEmailLoginFragment.afterTextChangeAction = onTextChangeListener
            editPasswordLoginFragment.afterTextChangeAction = onTextChangeListener
            logInButtonMainActivity.state = MainActionButtonState.DISABLED
            logInButtonMainActivity.setOnClickListener {
                viewModel.tryToLogIn(
                    editEmailLoginFragment.text ?: "",
                    editPasswordLoginFragment.text ?: ""
                )
                logInButtonMainActivity.state = MainActionButtonState.ENABLED
            }
        }
    }

    private val onTextChangeListener = {
        with(binding) {
            editEmailLoginFragment.textError = ""
            editPasswordLoginFragment.textError = ""
            if (!editEmailLoginFragment.text.isNullOrBlank() && !editPasswordLoginFragment.text.isNullOrBlank()) {
                logInButtonMainActivity.state = MainActionButtonState.ENABLED
            } else {
                logInButtonMainActivity.state = MainActionButtonState.DISABLED
            }
        }
    }
}