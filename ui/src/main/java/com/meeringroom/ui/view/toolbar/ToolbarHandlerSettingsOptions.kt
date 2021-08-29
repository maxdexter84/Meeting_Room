package com.meeringroom.ui.view.toolbar

import com.meetingroom.ui.R

sealed class ToolbarHandlerSettingsOptions(
    val title: String,
    var drawableImageId: Int,
    var onIconClick: () -> Unit
) {
    class More(title: String, onIconClick: () -> Unit) :
        ToolbarHandlerSettingsOptions(title, R.drawable.ic_baseline_add_icon, onIconClick)

    class AddEvent(title: String, onIconClick: () -> Unit) :
        ToolbarHandlerSettingsOptions(title, R.drawable.ic_baseline_more_vert, onIconClick)
}
