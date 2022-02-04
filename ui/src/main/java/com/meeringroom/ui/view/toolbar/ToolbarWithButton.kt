package com.meeringroom.ui.view.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.ToolbarWithButtonBinding

class ToolbarWithButton
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private val binding: ToolbarWithButtonBinding =
        ToolbarWithButtonBinding.inflate(
            LayoutInflater.from(context),
            this,
            true)

    val buttonCancel = binding.toolbarSaveCancel
    val lockButton = binding.buttonSaveToolbar
    val toolbarTitle = binding.toolbarSaveTitle

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ToolbarWithButton)
        binding.buttonSaveToolbar.textButton = attributes.getString(R.styleable.ToolbarWithButton_buttonText) ?: ""
        binding.toolbarSaveTitle.text = attributes.getString(R.styleable.ToolbarWithButton_toolbarTitle) ?: ""
        attributes.recycle()
    }
}