package com.meeringroom.ui.view.toolbar

import android.content.Context
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.MenuCompat
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.ToolbarBinding

class MrToolBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyle: Int = 0
) : Toolbar(context, attrs, defStyle) {
    private var binding: ToolbarBinding =
        ToolbarBinding.inflate(LayoutInflater.from(context), this, true)

    var mrToolbarTitle: String = ""
        set(value) {
            field = value
            binding.toolbarTitle.text = value
        }

    private var toolBarState: MrToolBarState = MrToolBarState.MORE
        set(value) {
            field = value
            changeToolBarLayout(field)
        }


    var configuration: ToolbarHandlerSettingsOptions =
        ToolbarHandlerSettingsOptions.More({}, {}, {})
        set(value) {
            field = value
            onLogOutClick = configuration.onLogOutClick
            onThemeColorsClick = configuration.onThemeColorsClick
            onLocationSettingsClick = configuration.onLocationSettingsClick
            onAddEventClick = configuration.onAddEventClick
            changeToolBarState(value)
        }

    private var onLogOutClick: () -> Unit = configuration.onLogOutClick
    private var onThemeColorsClick: () -> Unit = configuration.onThemeColorsClick
    private var onLocationSettingsClick: () -> Unit = configuration.onLocationSettingsClick
    private var onAddEventClick: () -> Unit = configuration.onAddEventClick

    init {
        setupAttributes(attrs, defStyle)
    }

    private fun setupAttributes(attrs: AttributeSet?, defStyle: Int) {
        context.withStyledAttributes(attrs, R.styleable.MrToolBar, defStyle, 0) {
            binding.toolbarTitle.text = getString(R.styleable.MrToolBar_setTitle) ?: ""
            mrToolbarTitle = binding.toolbarTitle.text.toString()
            toolBarState = MrToolBarState.values()[getInt(R.styleable.MrToolBar_bottomType, 0)]
            initConfiguration(toolBarState)
            binding.mrToolbar.setOnMenuItemClickListener {
                clickHandler(it)
            }
        }
    }

    private fun initConfiguration(toolBarState: MrToolBarState) {
        configuration = when (toolBarState) {
            MrToolBarState.MORE -> {
                ToolbarHandlerSettingsOptions.More({}, {}, {})
            }
            MrToolBarState.ADDEVENT -> {
                ToolbarHandlerSettingsOptions.AddEvent {}
            }
        }
    }

    private fun clickHandler(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.add_event -> onAddEventClick.invoke()
            R.id.location_settings -> onLocationSettingsClick.invoke()
            R.id.theme_colors -> onThemeColorsClick.invoke()
            R.id.log_out -> onLogOutClick.invoke()
        }
        return true
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

    private fun changeToolBarLayout(field: MrToolBarState) {
        binding.mrToolbar.menu.clear()
        binding.mrToolbar.inflateMenu(R.menu.base_menu)
        when (field) {
            MrToolBarState.MORE -> {
                MenuCompat.setGroupDividerEnabled(binding.mrToolbar.menu, true)
                for (i in 2 until binding.mrToolbar.menu.size()) {
                    val menuItem = binding.mrToolbar.menu.getItem(i)
                    val spannable =
                        SpannableString(binding.mrToolbar.menu.getItem(i).title.toString())
                    spannable.setSpan(
                        ForegroundColorSpan(
                            ResourcesCompat.getColor(
                                resources,
                                R.color.red_for_logout_text,
                                null
                            )
                        ), 0, spannable.length, 0
                    )
                    menuItem.title = spannable
                }
                binding.mrToolbar.dismissPopupMenus()
                binding.mrToolbar.menu.removeItem(R.id.add_event)
            }
            MrToolBarState.ADDEVENT -> {
                binding.mrToolbar.menu.removeGroup(R.id.group_of_items_1)
                binding.mrToolbar.menu.removeGroup(R.id.group_of_items_2)
            }
        }
    }

    sealed class ToolbarHandlerSettingsOptions(
        var onLogOutClick: () -> Unit,
        var onThemeColorsClick: () -> Unit,
        var onLocationSettingsClick: () -> Unit,
        var onAddEventClick: () -> Unit
    ) {
        class More(
            onLogOutClick: () -> Unit,
            onThemeColorsClick: () -> Unit,
            onLocationSettingsClick: () -> Unit,
        ) :
            ToolbarHandlerSettingsOptions(
                onLogOutClick,
                onThemeColorsClick,
                onLocationSettingsClick,
                onAddEventClick = {}
            )

        class AddEvent(
            onAddEventClick: () -> Unit
        ) : ToolbarHandlerSettingsOptions(
            onLogOutClick = {},
            onThemeColorsClick = {},
            onLocationSettingsClick = {},
            onAddEventClick
        )
    }
}