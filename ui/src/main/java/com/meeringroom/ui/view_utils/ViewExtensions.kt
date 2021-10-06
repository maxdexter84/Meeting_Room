package com.meeringroom.ui.view_utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

fun View.visibilityIf(isVisible: Boolean) {
        visibility = if(isVisible) View.VISIBLE else View.GONE
}

fun EditText.beforeTextChanged(listener: () -> Unit) {
        this.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        listener()
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(p0: Editable?) {}
        })
}

fun EditText.onTextChanged(listener: () -> Unit) {
        this.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        listener()
                }

                override fun afterTextChanged(p0: Editable?) {}
        })
}

fun EditText.afterTextChanged(listener: () -> Unit) {
        this.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                        listener()
                }
        })
}
