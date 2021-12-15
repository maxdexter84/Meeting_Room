package com.meetingroom.andersen.feature_landing.presentation.landing_fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.meetingroom.andersen.feature_landing.presentation.history_of_events_fragment.HistoryOfEventsFragment
import com.meetingroom.andersen.feature_landing.presentation.upcoming_events_fragment.UpcomingEventsFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val amountOfItems: Int
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return amountOfItems
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                UpcomingEventsFragment()
            }
            1 -> {
                HistoryOfEventsFragment()
            }
            else -> HistoryOfEventsFragment()
        }
    }

}