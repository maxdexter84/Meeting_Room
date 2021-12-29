package com.meetingroom.andersen.feature_landing.presentation.landing_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.core_module.component_manager.IHasComponent
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.deeplink_manager.DeeplinkNavigatorHelper
import com.google.android.material.tabs.TabLayoutMediator
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view.toolbar.ToolbarHandlerOptions
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentMySpaceBinding
import com.meetingroom.andersen.feature_landing.databinding.PopupWindowBinding
import com.meetingroom.andersen.feature_landing.presentation.di.DaggerLandingComponent
import com.meetingroom.andersen.feature_landing.presentation.di.LandingComponent
import javax.inject.Inject

class LandingFragment : BaseFragment<FragmentMySpaceBinding>(FragmentMySpaceBinding::inflate), IHasComponent<LandingComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: LandingFragmentViewModel by viewModels {
        viewModelFactory
    }
    private val deeplinkNavigatorHelper: DeeplinkNavigatorHelper by lazy {
        DeeplinkNavigatorHelper(findNavController())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.bindComponent(this).inject(this)
    }

    override fun getComponent(): LandingComponent {
        return DaggerLandingComponent
            .factory()
            .create(requireContext(), XInjectionManager.findComponent())
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
            bindingPopup.popupLocalSettings.setOnClickListener {
                deeplinkNavigatorHelper.navigate(DeeplinkNavigatorHelper.GO_TO_SET_LOCATION)
                dismiss()
            }
            bindingPopup.popupSwitchDarkTheme.setOnClickListener { dismiss() }
            bindingPopup.popupLogOut.setOnClickListener {
                viewModel.logout()
                deeplinkNavigatorHelper.navigate(DeeplinkNavigatorHelper.GO_TO_LOGIN_SCREEN)
                dismiss()
            }
            showAsDropDown(view, 215, 0)
        }
    }
}