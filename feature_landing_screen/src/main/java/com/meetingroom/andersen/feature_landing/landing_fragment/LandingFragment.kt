package com.meetingroom.andersen.feature_landing.landing_fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meeringroom.ui.view.toolbar.ToolbarHandlerOptions
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentLandingBinding
import com.meetingroom.andersen.feature_landing.databinding.PopupWindowBinding
import com.meetingroom.andersen.feature_landing.di.DaggerLandingFragmentComponent
import com.meetingroom.andersen.feature_landing.di.LandingFragmentModule
import javax.inject.Inject

class LandingFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLandingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            landingToolbar.setToolBarTitle(getString(R.string.toolbar_landing_title))
            landingToolbar.changeToolBarConfiguration(
                ToolbarHandlerOptions.More(
                    onIconClick = { showPopupWindow(landingToolbar.requireIconAsView()) }
                )
            )
        }
    }

    private fun showPopupWindow(view: View) {
        val popupWindow = PopupWindow(requireActivity())
        val bindingPopup = PopupWindowBinding.inflate(LayoutInflater.from(requireContext()))
        with(popupWindow) {
            contentView = bindingPopup.root
            height = WindowManager.LayoutParams.WRAP_CONTENT
            width = WindowManager.LayoutParams.WRAP_CONTENT
            isOutsideTouchable = true
            isFocusable = true
            elevation = 20f
            setBackgroundDrawable(null)
            bindingPopup.popupLocalSettings.setOnClickListener { dismiss() }
            bindingPopup.popupThemeColour.setOnClickListener { dismiss() }
            bindingPopup.popupLogOut.setOnClickListener {
                viewModel.logout()
                dismiss()
            }
            overlapAnchor = true
            showAsDropDown(view, 200, 0)
        }
    }
}