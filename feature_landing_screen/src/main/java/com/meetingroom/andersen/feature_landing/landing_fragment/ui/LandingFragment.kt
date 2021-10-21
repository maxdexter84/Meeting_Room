package com.meetingroom.andersen.feature_landing.landing_fragment.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.google.android.material.tabs.TabLayoutMediator
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view.toolbar.ToolbarHandlerOptions
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentMySpaceBinding
import com.meetingroom.andersen.feature_landing.databinding.PopupWindowBinding
import com.meetingroom.andersen.feature_landing.di.landing_fragment.DaggerLandingFragmentComponent
import com.meetingroom.andersen.feature_landing.di.landing_fragment.LandingFragmentModule
import com.meetingroom.andersen.feature_landing.landing_fragment.presentation.LandingFragmentViewModel
import javax.inject.Inject

class LandingFragment : BaseFragment<FragmentMySpaceBinding>(FragmentMySpaceBinding::inflate) {

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
        initToolbar()
        initViewPager()
    }

    private fun initViewPager() {
        with(binding) {
            viewPager.adapter = ViewPagerAdapter(
                childFragmentManager,
                lifecycle,
                resources.getStringArray(R.array.titles_for_pages_my_Space).size
            )

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = resources.getStringArray(R.array.titles_for_pages_my_Space)[position]
            }.attach()
        }
    }

    private fun initToolbar() {
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
            overlapAnchor = true
            elevation = 20f
            setBackgroundDrawable(null)
            bindingPopup.popupLocalSettings.setOnClickListener { dismiss() }
            bindingPopup.popupThemeColour.setOnClickListener { dismiss() }
            bindingPopup.popupLogOut.setOnClickListener {
                viewModel.logout()
                dismiss()
            }
            showAsDropDown(view, 215, 0)
        }
    }
}