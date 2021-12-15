package com.meetingroom.feature_login.di

import android.content.Context
import com.example.core_module.di.FeatureScope
import com.example.core_network.NetworkModule
import com.meetingroom.feature_login.LoginFragment
import com.meetingroom.feature_login.LoginFragmentViewModel
import com.meetingroom.feature_login.di.view_model.ViewModelModule
import dagger.BindsInstance
import dagger.Component

@Component(modules = [ ViewModelModule::class], dependencies = [LoginDeps::class])
@FeatureScope
interface LoginComponent {

    fun inject(loginFragment: LoginFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context, loginDeps: LoginDeps): LoginComponent
    }
}