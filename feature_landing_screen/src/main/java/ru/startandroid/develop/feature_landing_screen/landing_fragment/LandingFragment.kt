package ru.startandroid.develop.feature_landing_screen.landing_fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import ru.startandroid.develop.feature_landing_screen.R
import ru.startandroid.develop.feature_landing_screen.databinding.FragmentLandingBinding
import ru.startandroid.develop.feature_landing_screen.di.DaggerLandingFragmentComponent
import ru.startandroid.develop.feature_landing_screen.di.LandingFragmentModule
import javax.inject.Inject

class LandingFragment : Fragment(R.layout.fragment_landing) {

    private lateinit var binding: FragmentLandingBinding
    @Inject
    lateinit var viewModel: LandingFragmentViewModel

    override fun onAttach(context: Context) {
        DaggerLandingFragmentComponent.builder()
            .landingFragmentModule(LandingFragmentModule(this))
            .sharedPreferencesModule(SharedPreferencesModule(requireContext()))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLandingBinding.bind(view)

        with(binding) {

        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.isDeleteRequired()
    }
}