package com.meeringroom.ui.view_utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.visibilityIf(isVisible: Boolean) {
        visibility = if(isVisible) View.VISIBLE else View.GONE
}

fun View.hideKeyboard(context: Context) {
        val imn = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imn.hideSoftInputFromWindow(this.windowToken, 0)
}