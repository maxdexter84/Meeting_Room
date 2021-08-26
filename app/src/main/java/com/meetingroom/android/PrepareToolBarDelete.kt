package com.meetingroom.android

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.meeringroom.ui.view.toolbar.MrToolBar

class PrepareToolBarDelete : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val a = inflater.inflate(R.layout.fragment_prepare_tool_bar_delete, container, false)
        val b = a.findViewById<MrToolBar>(R.id.tool_bar)
        b.onLocationSettingsClick =
            { findNavController().navigate(R.id.action_PrepareToolBarDelete_to_nav_between_locations_fragment) }
        b.onAddEventClick = { Log.e("forMax", "onAddEventClick") }
        return a
    }


}