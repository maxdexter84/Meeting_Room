package com.meeringroom.ui.view.toolbar

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
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

    private var inputType: MrToolBarState = MrToolBarState.MORE
        set(value) {
            field = value
            when (field) {
                MrToolBarState.MORE -> {
                    MenuCompat.setGroupDividerEnabled(binding.mrToolbar.menu, true)
                    for (i in 2 until binding.mrToolbar.menu.size()) {
                        val menuItem = binding.mrToolbar.menu.getItem(i)
                        val spannable =
                            SpannableString(binding.mrToolbar.menu.getItem(i).title.toString())
                        spannable.setSpan(ForegroundColorSpan(Color.RED), 0, spannable.length, 0)
                        menuItem.title = spannable
                    }
                    binding.mrToolbar.dismissPopupMenus()
                    binding.mrToolbar.menu.removeItem(R.id.add_event)
                }
                MrToolBarState.ADD -> {
                    binding.mrToolbar.menu.removeGroup(R.id.group_of_items_1)
                    binding.mrToolbar.menu.removeGroup(R.id.group_of_items_2)
                }

            }
        }
    var onLogOutClick: () -> Unit = {}
    var onThemeColorsClick: () -> Unit = {}
    var onLocationSettingsClick: () -> Unit = {}
    var onAddEventClick: () -> Unit = {}

    init {
        setupAttributes(attrs, defStyle)
    }

    private fun setupAttributes(attrs: AttributeSet?, defStyle: Int) {
        context.withStyledAttributes(attrs, R.styleable.MrToolBar, defStyle, 0) {
            binding.toolbarTitle.text = getString(R.styleable.MrToolBar_setTitle) ?: ""
            inputType = MrToolBarState.values()[getInt(R.styleable.MrToolBar_bottomType, 0)]

            binding.mrToolbar.setOnMenuItemClickListener {
                clickHandler(it)
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
}