package com.meetingroom.andersen.feature_landing_screen.landing_fragment

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuCompat
import androidx.fragment.app.Fragment
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meeringroom.ui.view.toolbar.ToolbarHandlerOptions
import com.meetingroom.andersen.feature_landing_screen.R
import com.meetingroom.andersen.feature_landing_screen.databinding.FragmentLandingBinding
import com.meetingroom.andersen.feature_landing_screen.di.DaggerLandingFragmentComponent
import com.meetingroom.andersen.feature_landing_screen.di.LandingFragmentModule
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
            landingToolbar.setToolBarTitle(getString(R.string.toolbar_landing_title))
            landingToolbar.changeToolBarConfiguration(
                ToolbarHandlerOptions.More(
                    onIconClick = { showPopUpMenu(landingToolbar.requireIconAsView()) }
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.isDeleteRequired()
    }

    private fun showPopUpMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.base_menu)
        MenuCompat.setGroupDividerEnabled(popupMenu.menu, true)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.location_settings -> true
                R.id.theme_colors -> true
                R.id.log_out -> {
                    viewModel.logout()
                    true
                }
                else -> false
            }
        }
        for (i in 2 until popupMenu.menu.size()) {
            val menuItem = popupMenu.menu.getItem(i)
            val spannable =
                SpannableString(popupMenu.menu.getItem(i).title.toString())
            spannable.setSpan(
                ForegroundColorSpan(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.red_for_logout_text,
                        null
                    )
                ), 0, spannable.length, 0
            )
            menuItem.title = spannable
        }
        popupMenu.show()
    }
}