package com.meeringroom.ui.view.toolbar

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.PopupMenu
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
    private var inputType = 0

    init {
        setupAttributes(attrs, defStyle)
    }

    private fun setupAttributes(attrs: AttributeSet?, defStyle: Int) {
        context.withStyledAttributes(attrs, R.styleable.MrToolBar, defStyle, 0) {
            inputType = getInt(R.styleable.MrToolBar_bottomType, -1)
            binding.toolbarTitle.text = getString(R.styleable.MrToolBar_setTitle) ?: ""
            if (inputType == 1) {
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
            } else {
                binding.mrToolbar.menu.removeGroup(R.id.group_of_items_1)
                binding.mrToolbar.menu.removeGroup(R.id.group_of_items_2)
            }

        }

    }
}