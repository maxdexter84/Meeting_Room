package com.meetingroom.feature_login

import androidx.lifecycle.ViewModel
import com.meetingroom.feature_login.annotation.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Subcomponent(modules = [LoginFragmentModule::class])
interface LoginFragmentComponent {
    fun inject(loginFragment: LoginFragment)
}

@Module
interface LoginFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginFragmentViewModel::class)
    fun bindLoginFragmentViewModel(viewModel: LoginFragmentViewModel): ViewModel
}