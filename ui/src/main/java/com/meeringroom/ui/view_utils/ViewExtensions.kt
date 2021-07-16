package com.meeringroom.ui.view_utils

import android.view.View

class ViewExtensions {

    companion object {
        fun View.visibilityIf(isVisible: Boolean) {
            visibility = if(isVisible) View.VISIBLE else View.GONE
        }
    }
}