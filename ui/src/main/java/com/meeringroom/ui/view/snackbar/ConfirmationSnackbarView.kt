package com.meeringroom.ui.view.snackbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.ContentViewCallback
import com.meetingroom.ui.R

class ConfirmationSnackbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), ContentViewCallback {

    private val icon: ImageView
    private val textView: TextView

    init {
        View.inflate(context, R.layout.view_confirmation_snackbar, this)
        this.icon = findViewById(R.id.confirmation_snackbar_icon)
        this.textView = findViewById(R.id.confirmation_snackbar_textview)
        clipToPadding = false
    }

    fun setMessage(message: String) {
        this.textView.text = message
    }

    override fun animateContentIn(delay: Int, duration: Int) {
    }

    override fun animateContentOut(delay: Int, duration: Int) {
    }
}