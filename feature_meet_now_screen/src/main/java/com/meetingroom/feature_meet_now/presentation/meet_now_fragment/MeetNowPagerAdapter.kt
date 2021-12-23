package com.meetingroom.feature_meet_now.presentation.meet_now_fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.meetingroom.feature_meet_now.presentation.available_now_fragment.RoomsAvailableNowFragment
import com.meetingroom.feature_meet_now.presentation.available_soon_fragment.RoomsAvailableSoonFragment

private const val ITEM_COUNT = 2
private const val AVAILABLE_NOW = 0
private const val AVAILABLE_SOON = 1

class MeetNowPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return ITEM_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            AVAILABLE_NOW -> RoomsAvailableNowFragment()
            AVAILABLE_SOON -> RoomsAvailableSoonFragment()
            else -> RoomsAvailableNowFragment()
        }
    }
}