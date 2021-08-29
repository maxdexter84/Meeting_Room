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

    private var toolBarState: MrToolBarState = MrToolBarState.MORE

    private var configuration: ToolbarHandlerSettingsOptions =
        ToolbarHandlerSettingsOptions.AddEvent("") {}
        set(value) {
            field = value
            changeToolBarState(value)
            binding.mrtoolbarTitle.text = value.title
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
            toolBarState = MrToolBarState.values()[getInt(R.styleable.MrToolBar_bottomType, 0)]
            initConfiguration(toolBarState)
        }
    }

    private fun initConfiguration(toolBarState: MrToolBarState) {
        when (toolBarState) {
            MrToolBarState.MORE -> {
                configuration =
                    ToolbarHandlerSettingsOptions.More(binding.mrtoolbarTitle.text.toString()) {}
                binding.mrtoolbarIcon.setButtonDrawable(configuration.drawableImageId)
            }
            MrToolBarState.ADDEVENT -> {
                configuration =
                    ToolbarHandlerSettingsOptions.AddEvent(binding.mrtoolbarTitle.text.toString()) {}
                binding.mrtoolbarIcon.setButtonDrawable(configuration.drawableImageId)
            }
        }
    }

    private fun changeToolBarState(type: ToolbarHandlerSettingsOptions) {
        toolBarState = when (type) {
            is ToolbarHandlerSettingsOptions.AddEvent -> {
                MrToolBarState.ADDEVENT
            }
            is ToolbarHandlerSettingsOptions.More -> {
                MrToolBarState.MORE
            }
        }
    }

    fun changeToolBarConfiguration(newConfiguration: ToolbarHandlerSettingsOptions) {
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