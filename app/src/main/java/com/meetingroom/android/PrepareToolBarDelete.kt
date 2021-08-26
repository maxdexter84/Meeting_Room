package com.meetingroom.android

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

class PrepareToolBarDelete : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_prepare_tool_bar_delete, container, false)
    }

}