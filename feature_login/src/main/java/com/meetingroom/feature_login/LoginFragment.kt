package com.meetingroom.feature_login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meetingroom.feature_login.base.BaseFragment
import com.meetingroom.feature_login.databinding.LoginFragmentBinding
import com.meetingroom.feature_login.di.Injector

class LoginFragment: BaseFragment<LoginFragmentViewModel>() {

    lateinit var binding: LoginFragmentBinding

    override fun onAttach(context: Context) {
        Injector.loginFragmentComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun injectViewModel() {
        viewModel = getViewModel()
    }
}