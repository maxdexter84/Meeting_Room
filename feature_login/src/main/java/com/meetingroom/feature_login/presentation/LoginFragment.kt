package com.meetingroom.feature_login.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.core_module.component_manager.IHasComponent
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.deeplink_manager.DeeplinkNavigatorHelper
import com.example.core_network.RequestMaker
import com.example.core_network.RequestResult
import com.meeringroom.ui.view.base_classes.BaseActivity
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view.login_button.MainActionButtonState
import com.meetingroom.feature_login.R
import com.meetingroom.feature_login.databinding.LoginFragmentBinding
import com.meetingroom.feature_login.di.DaggerLoginComponent
import com.meetingroom.feature_login.di.LoginComponent
import javax.inject.Inject

class LoginFragment : BaseFragment<LoginFragmentBinding>(LoginFragmentBinding::inflate),
    IHasComponent<LoginComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: LoginFragmentViewModel by viewModels {
        viewModelFactory
    }


    private val deeplinkNavigatorHelper: DeeplinkNavigatorHelper by lazy {
        DeeplinkNavigatorHelper(findNavController())
    }
    private lateinit var baseActivity: BaseActivity<*>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.bindComponent(this).inject(this)
        baseActivity = activity as BaseActivity<*>
    }

    override fun getComponent(): LoginComponent {
        return DaggerLoginComponent
            .factory()
            .create(requireContext(), XInjectionManager.findComponent())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel.loginRequestResult.observe(viewLifecycleOwner, {
                when (it) {
                    is RequestResult.Loading -> logInButtonMainActivity.state =
                        MainActionButtonState.LOADING
                    is RequestResult.Success -> {
                        deeplinkNavigatorHelper.navigate(DeeplinkNavigatorHelper.GO_TO_SET_LOCATION)
                    }
                    is RequestResult.Error -> {
                        when (it.code) {
                            RequestMaker.DEFAULT_EXCEPTION_CODE -> {
                                baseActivity.showDialog(
                                    R.string.error_title_no_internet,
                                    R.string.error_description_no_internet,
                                    false,
                                    R.string.got_it)
                            }
                            else -> showErrorIncorrectEmailOrPassword()
                        }
                        logInButtonMainActivity.state = MainActionButtonState.DISABLED
                    }
                }
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

    private fun showErrorIncorrectEmailOrPassword() {
        with(requireContext().getString(R.string.error_for_log_in)) {
            binding.editEmailLoginFragment.textError = this
            binding.editPasswordLoginFragment.textError = this
        }
    }
}