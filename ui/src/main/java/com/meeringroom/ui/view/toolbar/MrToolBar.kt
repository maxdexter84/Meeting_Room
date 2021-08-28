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

    var configuration: ToolbarHandlerSettingsOptions =
        ToolbarHandlerSettingsOptions.AddEvent("") {}
        set(value) {
            field = value
            changeToolBarState(value)
            binding.toolbarTitle.text = value.title
            binding.icon.setOnClickListener {
                configuration.onIconClick.invoke()
            }
        }

    init {
        setupAttributes(attrs, defStyle)
    }

    private fun setupAttributes(attrs: AttributeSet?, defStyle: Int) {
        context.withStyledAttributes(attrs, R.styleable.MrToolBar, defStyle, 0) {
            binding.toolbarTitle.text = getString(R.styleable.MrToolBar_setTitle) ?: ""
            toolBarState = MrToolBarState.values()[getInt(R.styleable.MrToolBar_bottomType, 0)]
            initConfiguration(toolBarState)
        }
    }

    private fun initConfiguration(toolBarState: MrToolBarState) {
        when (toolBarState) {
            MrToolBarState.MORE -> {
                configuration =
                    ToolbarHandlerSettingsOptions.More(binding.toolbarTitle.text.toString()) {}
                binding.icon.setButtonDrawable(configuration.drawableImageId)
            }
            MrToolBarState.ADDEVENT -> {
                configuration =
                    ToolbarHandlerSettingsOptions.AddEvent(binding.toolbarTitle.text.toString()) {}
                binding.icon.setButtonDrawable(configuration.drawableImageId)
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

    fun requireIconAsView(): View {
        return binding.icon
    }

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
}