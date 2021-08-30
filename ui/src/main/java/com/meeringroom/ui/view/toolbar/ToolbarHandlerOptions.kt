package com.meeringroom.ui.view.toolbar

import com.meetingroom.ui.R

sealed class ToolbarHandlerOptions(
    var drawableImageId: Int,
    var onIconClick: () -> Unit
) {
    class More(onIconClick: () -> Unit) :
        ToolbarHandlerOptions(R.drawable.ic_baseline_more_vert, onIconClick)

    class AddEvent(onIconClick: () -> Unit) :
        ToolbarHandlerOptions(R.drawable.ic_baseline_add_icon, onIconClick)
}
