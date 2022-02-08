package com.meeringroom.ui.view.snackbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.meetingroom.ui.R

private const val EMPTY_MESSAGE = ""

class ConfirmationSnackbar(
    parent: ViewGroup,
    val content: ConfirmationSnackbarView
) : BaseTransientBottomBar<ConfirmationSnackbar>(parent, content, content) {

    var message: String = EMPTY_MESSAGE
    set(value) {
        field = value
        content.setMessage(value)
    }

    init {
        getView().setBackgroundColor(
            context.resources.getColor(R.color.toolbar_background)
        )
    }

    companion object {

        fun make(view: View): ConfirmationSnackbar {

            val parent = findSuitableParent(view) ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            val customView = LayoutInflater.from(view.context).inflate(
                R.layout.layout_confirmation_snackbar,
                parent,
                false
            ) as ConfirmationSnackbarView

            return ConfirmationSnackbar(
                parent,
                customView
            )
        }

        private fun findSuitableParent(view: View?): ViewGroup? {
            var view = view
            var fallback: ViewGroup? = null
            do {
                if (view is CoordinatorLayout) {
                    return view
                } else if (view is FrameLayout) {
                    if (view.id == android.R.id.content) {
                        return view
                    } else {
                        fallback = view
                    }
                }

                if (view != null) {
                    val parent = view.parent
                    view = if (parent is View) parent else null
                }
            } while (view != null)

            return fallback
        }
    }
}