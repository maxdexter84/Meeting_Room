package com.meeringroom.ui.view.login_button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.meeringroom.ui.view_utils.ViewExtensions.Companion.visibilityIf
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.LoginButtonLayoutBinding

class LogInButtonMainActivity @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var state: LogInButtonMainActvityState = LogInButtonMainActvityState.DISABLED
    set(value) {
        field = value
        when(field) {
            LogInButtonMainActvityState.ENABLED -> {
                binding.logInTextMainActivity.isEnabled = true
                isEnabled = true
                isClickable = true
                setBackgroundResource(R.drawable.button_enabled_shape)
            }
            LogInButtonMainActvityState.DISABLED -> {
                binding.logInTextMainActivity.isEnabled = false
                isEnabled = false
                isClickable = false
                setBackgroundResource(R.drawable.button_disabled_shape)
            }
            LogInButtonMainActvityState.LOADING -> {
                isClickable = false
                binding.logInTextMainActivity.visibilityIf(false)
                binding.logInProgressIndicatorMainActivity.visibilityIf(true)
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

        context.withStyledAttributes(attrs, R.styleable.ProgressButton, defStyleAttr, 0) {
            textButton = getString(R.styleable.ProgressButton_text).toString()
            state = LogInButtonMainActvityState.values()[getInt(R.styleable.ProgressButton_state, 0)]
        }
    }
}