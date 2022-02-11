package com.meetingroom.andersen.feature_landing.presentation.landing_fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.core_module.component_manager.IHasComponent
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.deeplink_manager.DeeplinkNavigatorHelper
import com.example.core_module.sharedpreferences.SharedPreferencesKeys
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.google.android.material.tabs.TabLayoutMediator
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view.snackbar.ConfirmationSnackbar
import com.meeringroom.ui.view.toolbar.ToolbarHandlerOptions
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentMySpaceBinding
import com.meetingroom.andersen.feature_landing.databinding.PopupWindowBinding
import com.meetingroom.andersen.feature_landing.presentation.di.DaggerLandingComponent
import com.meetingroom.andersen.feature_landing.presentation.di.LandingComponent
import javax.inject.Inject

class LandingFragment : BaseFragment<FragmentMySpaceBinding>(FragmentMySpaceBinding::inflate),
    IHasComponent<LandingComponent> {

    @Inject
    lateinit var sharedPref: UserDataPrefHelper
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
        observeSuccessChanged()
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
        initTheme(bindingPopup)
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
            bindingPopup.popupSwitchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
                initThemeListener(isChecked)
            }
            bindingPopup.popupLogOut.setOnClickListener {
                viewModel.logout()
                deeplinkNavigatorHelper.navigate(DeeplinkNavigatorHelper.GO_TO_LOGIN_SCREEN)
                dismiss()
            }
            showAsDropDown(view, 215, 0)
        }
    }

    private fun initTheme(bindingPopup: PopupWindowBinding) {
        when (getSavedTheme()) {
            THEME_LIGHT -> bindingPopup.popupSwitchDarkTheme.isChecked = false
            THEME_DARK -> bindingPopup.popupSwitchDarkTheme.isChecked = true
            THEME_UNDEFINED -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> bindingPopup.popupSwitchDarkTheme.isChecked = false
                    Configuration.UI_MODE_NIGHT_YES -> bindingPopup.popupSwitchDarkTheme.isChecked = true
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> bindingPopup.popupSwitchDarkTheme.isChecked = false
                }
            }
        }
    }

    private fun initThemeListener(isChecked: Boolean) {
        when (isChecked) {
            false -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
            true -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
        }
    }

    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        sharedPref.saveTheme(SharedPreferencesKeys.KEY_THEME,prefsMode)
    }


    private fun getSavedTheme() = sharedPref.getTheme()

    private fun observeSuccessChanged() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            SUCCESS_KEY
        )
            ?.observe(viewLifecycleOwner) {
                showSuccessChanged(resources.getString(R.string.success_changes))
            }
    }

    private fun showSuccessChanged(snackbarMessage: String) {
        ConfirmationSnackbar.make(binding.root).apply {
            message = snackbarMessage
        }
            .setAnchorView(R.id.my_space_screen_navigation)
            .show()
    }


    companion object {
        const val THEME_UNDEFINED = -1
        const val THEME_LIGHT = 0
        const val THEME_DARK = 1
        const val THEME_SYSTEM = 2
        const val SUCCESS_KEY = "CHANGED"
    }
}