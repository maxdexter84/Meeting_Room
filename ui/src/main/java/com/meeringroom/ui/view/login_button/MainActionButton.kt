package com.meeringroom.ui.view.login_button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.meeringroom.ui.view_utils.visibilityIf
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.LoginButtonLayoutBinding

class MainActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var state: MainActionButtonState = MainActionButtonState.DISABLED
        set(value) {
            field = value
            when (field) {
                MainActionButtonState.ENABLED -> {
                    binding.logInTextMainActivity.isVisible = true
                    isEnabled = true
                    isClickable = true
                    binding.logInProgressIndicatorMainActivity.visibilityIf(false)
                    setBackgroundResource(R.drawable.button_enabled_shape)
                    binding.logInTextMainActivity.isEnabled = true
                }
                MainActionButtonState.DISABLED -> {
                    binding.logInTextMainActivity.isVisible = true
                    isEnabled = false
                    isClickable = false
                    binding.logInProgressIndicatorMainActivity.visibilityIf(false)
                    setBackgroundResource(R.drawable.button_disabled_shape)
                    binding.logInTextMainActivity.isEnabled = false
                }
                MainActionButtonState.LOADING -> {
                    isClickable = false
                    binding.logInTextMainActivity.isVisible = false
                    binding.logInProgressIndicatorMainActivity.visibilityIf(true)
                    setBackgroundResource(R.drawable.button_enabled_shape)
                }
            }
        }

    private var textButton: String = ""
        set(value) {
            field = value
            binding.logInTextMainActivity.text = field
        }

    private var binding: LoginButtonLayoutBinding =
        LoginButtonLayoutBinding.inflate(LayoutInflater.from(context), this)

    init {
        loadAttr(attrs, defStyleAttr)
    }

    private fun loadAttr(attrs: AttributeSet?, defStyleAttr: Int) {

        context.withStyledAttributes(attrs, R.styleable.MainActionButton, defStyleAttr, 0) {
            textButton = getString(R.styleable.MainActionButton_text).toString()
            state = MainActionButtonState.values()[getInt(R.styleable.MainActionButton_state, 0)]
        }
    }
}