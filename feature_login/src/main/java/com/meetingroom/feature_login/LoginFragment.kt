package com.meetingroom.feature_login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.meetingroom.feature_login.databinding.LoginFragmentBinding
import com.meetingroom.feature_login.di.DaggerLoginComponent
import com.meetingroom.feature_login.di.LoginFragmentModule
import javax.inject.Inject

class LoginFragment : Fragment() {

    lateinit var binding: LoginFragmentBinding

    @Inject
    lateinit var viewModel: LoginFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerLoginComponent.builder()
            .loginFragmentModule(LoginFragmentModule(this))
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        binding.logInButtonMainActivity.setOnClickListener {
        
            if (viewModel.tryToLogIn(binding.editEmailLoginFragment.text, binding.editPasswordLoginFragment.text)) {
            findNavController().navigate(R.id.action_loginFragment_to_next_after_login)
            } else {
                binding.editEmailLoginFragment.textError = "Wrong Email Or password"
            }

        }

        return binding.root
    }

}