package com.meetingroom.feature_meet_now.presentation.meet_now_fragment

import android.os.Bundle
import android.view.View
import com.example.core_module.component_manager.IHasComponent
import com.example.core_module.component_manager.XInjectionManager
import com.google.android.material.tabs.TabLayoutMediator
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meetingroom.feature_meet_now.presentation.di.DaggerMeetNowComponent
import com.meetingroom.feature_meet_now.presentation.di.MeetNowComponent
import com.meetingroom.feature_meet_now_screen.R
import com.meetingroom.feature_meet_now_screen.databinding.FragmentMeetNowBinding
import kotlinx.android.synthetic.main.fragment_meet_now.tabLayout
import kotlinx.android.synthetic.main.fragment_meet_now.viewPager

class MeetNowFragment : BaseFragment<FragmentMeetNowBinding>(FragmentMeetNowBinding::inflate), IHasComponent<MeetNowComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.bindComponent(this).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViewPager()
    }

    override fun getComponent(): MeetNowComponent {
        return DaggerMeetNowComponent
            .factory()
            .create(requireContext(), XInjectionManager.findComponent())
    }

    private fun initToolbar() {
        with(binding) {
            meetNowToolbar.apply {
                setToolBarTitle(getString(R.string.meet_now_title))
                showIcon(false)
            }
        }
    }

    private fun initViewPager() {
        with(binding) {
            viewPager.adapter = MeetNowPagerAdapter(childFragmentManager, lifecycle)
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.tab_titles)[position]
        }.attach()
    }
}