package com.meeringroom.ui.event_dialogs.dialog_server_time_out

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.CustomDialogTimeOutServerBinding

class ServerTimeOutDialog :
    BaseDialogFragment<CustomDialogTimeOutServerBinding>(CustomDialogTimeOutServerBinding::inflate) {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = onCreateView(LayoutInflater.from(requireContext()), null, savedInstanceState)
        return createBaseDialogBuilder(
            R.string.time_out_server_dialog_title,
            R.string.time_out_server_dialog_message,
            R.string.time_out_server_dialog_button_positive,
            R.string.time_out_server_dialog_button_negative,
            {dismiss()},
            {findNavController().apply { popBackStack()
            popBackStack()}}
        ).setView(dialogView).create()
    }


    private fun createBaseDialogBuilder(
        titleId: Int,
        messageId: Int,
        positiveButtonId: Int,
        negativeButtonId: Int,
        onPositiveClickListener: () -> Unit,
        onNegativeClickListener: () -> Unit
    ) = MaterialAlertDialogBuilder(requireContext()).setTitle(titleId).setMessage(messageId)
        .setPositiveButton(positiveButtonId) { _: DialogInterface, _: Int -> onPositiveClickListener() }
        .setNegativeButton(negativeButtonId) { _: DialogInterface, _: Int -> onNegativeClickListener() }
}