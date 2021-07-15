package com.meeringroom.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.ButtonLayoutBinding

class ProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val progressIndicator: CircularProgressIndicator
    private val buttonTextView: TextView
    private var state: ProgressButtonState = ProgressButtonState.DISABLED
    private val binding: ButtonLayoutBinding =
        ButtonLayoutBinding.inflate(LayoutInflater.from(context), this)

    init {
        buttonTextView = binding.buttonText
        progressIndicator = binding.progressIndicator
        loadAttr(attrs, defStyleAttr)
    }

    private fun loadAttr(attrs: AttributeSet?, defStyleAttr: Int) {

        context.withStyledAttributes(attrs, R.styleable.ProgressButton, defStyleAttr, 0) {
            val buttonText = getString(R.styleable.ProgressButton_text)
            val loading = getBoolean(R.styleable.ProgressButton_loading, false)
            val enabled = getBoolean(R.styleable.ProgressButton_enabled, true)
            isEnabled = enabled
            buttonTextView.isEnabled = enabled
            setText(buttonText)
            setLoading(loading)
        }
    }

    private fun setLoading(loading: Boolean) {
        isClickable = !loading
        if(loading){
            buttonTextView.visibleOrGone(!loading)
            progressIndicator.visibleOrGone(loading)
            state = ProgressButtonState.LOADING
        }
    }

    private fun View.visibleOrGone(visible: Boolean) {
        visibility = if(visible) View.VISIBLE else View.GONE
    }

    private fun setText(text : String?) {
        buttonTextView.text = text
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        buttonTextView.isEnabled = enabled
        state = if(enabled) ProgressButtonState.ENABLED else ProgressButtonState.DISABLED
    }

    fun onClick() {
        when(state) {
            ProgressButtonState.DISABLED -> {
                TODO()
            }
            ProgressButtonState.ENABLED -> {
                TODO()
            }
            ProgressButtonState.LOADING -> {
                TODO()
            }
        }
    }
}