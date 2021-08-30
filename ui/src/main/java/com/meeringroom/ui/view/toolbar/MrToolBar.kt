package com.meeringroom.ui.view.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.ToolbarBinding

class MrToolBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {
    private var binding: ToolbarBinding =
        ToolbarBinding.inflate(LayoutInflater.from(context), this, true)


    private var configuration: ToolbarHandlerOptions =
        ToolbarHandlerOptions.AddEvent {}
        set(value) {
            field = value
            binding.mrtoolbarIcon.setButtonDrawable(value.drawableImageId)
            binding.mrtoolbarIcon.setOnClickListener {
                configuration.onIconClick.invoke()
            }
        }

    init {
        setupAttributes(attrs, defStyle)
    }

    private fun setupAttributes(attrs: AttributeSet?, defStyle: Int) {
        context.withStyledAttributes(attrs, R.styleable.MrToolBar, defStyle, 0) {
            binding.mrtoolbarTitle.text = getString(R.styleable.MrToolBar_setTitle) ?: ""

            changeConfiguration(getInt(R.styleable.MrToolBar_bottomType, 0))
        }
    }

    private fun changeConfiguration(toolBarState: Int) {
        when (toolBarState) {
            0 -> {
                configuration =
                    ToolbarHandlerOptions.More {}
                binding.mrtoolbarIcon.setButtonDrawable(configuration.drawableImageId)
            }
            1 -> {
                configuration =
                    ToolbarHandlerOptions.AddEvent {}
                binding.mrtoolbarIcon.setButtonDrawable(configuration.drawableImageId)
            }
        }
    }


    fun changeToolBarConfiguration(newConfiguration: ToolbarHandlerOptions) {
        configuration = newConfiguration
    }

    fun requireIconAsView(): View {
        return binding.mrtoolbarIcon
    }

    fun getToolBarTitle(): String = binding.mrtoolbarTitle.text.toString()

    fun setToolBarTitle(title: String) {
        binding.mrtoolbarTitle.text = title
    }

}