package com.meetingroom.feature_workplaces_screen.presentation.di

import android.content.Context
import com.example.core_module.di.FeatureScope
import com.meetingroom.feature_workplaces_screen.presentation.di.viewmodel.ViewModelModule
import com.meetingroom.feature_workplaces_screen.presentation.fragments.workplaces_fragment.WorkplacesFragment
import dagger.BindsInstance
import dagger.Component

@FeatureScope
@Component(modules = [
    ViewModelModule::class
])
interface WorkplacesComponent {

    fun inject(workplacesFragment: WorkplacesFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): WorkplacesComponent
    }
}