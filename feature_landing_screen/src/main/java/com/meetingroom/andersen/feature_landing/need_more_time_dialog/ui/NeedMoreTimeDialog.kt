package com.meetingroom.andersen.feature_landing.need_more_time_dialog.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.CustomDialogNeedMoreTimeBinding


class NeedMoreTimeDialog:  BaseDialogFragment<CustomDialogNeedMoreTimeBinding>(
    CustomDialogNeedMoreTimeBinding::inflate)  {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = onCreateView(LayoutInflater.from(requireContext()), null, savedInstanceState)
        val tvTimeLeft = binding.textViewDialogTimeLeft.apply {
            text = "15"
        }
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.need_more_time_dialog_title)
            .setMessage(R.string.need_more_time_dialog_message)
            .setView(dialogView)
            .setCancelable(false)
            .setPositiveButton(R.string.need_more_time_dialog_button) { _: DialogInterface, _: Int -> }
            .create()
    }
}
