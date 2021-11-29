package com.meeringroom.ui.dialog_need_more_time

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.CustomDialogNeedMoreTimeBinding

class NeedMoreTimeDialog:  BaseDialogFragment<CustomDialogNeedMoreTimeBinding>(
    CustomDialogNeedMoreTimeBinding::inflate)  {

    private lateinit var timer: CountDownTimer

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = onCreateView(LayoutInflater.from(requireContext()), null, savedInstanceState)
        val secondsTextView = binding.textViewDialogTimeLeft.apply {
            text = (TIME_LIMIT_MILLIS / TIME_STEP_MILLIS).toString()
        }

        timer = object: CountDownTimer(TIME_LIMIT_MILLIS, TIME_STEP_MILLIS) {
            override fun onTick(millisUntilFinished: Long) {
                secondsTextView.text = ((millisUntilFinished + TIME_STEP_MILLIS) / TIME_STEP_MILLIS).toInt().toString()
            }
            override fun onFinish() {
                context?.let {
                    showExpiredSessionDialog()
                }
            }
        }.start()

        return  createBaseDialogBuilder(
            R.string.need_more_time_dialog_title,
            R.string.need_more_time_dialog_message,
            R.string.need_more_time_dialog_button) {
            dismiss()
        }
            .setView(dialogView)
            .create()
    }

    private fun showExpiredSessionDialog() {
        dialog?.hide()
        createBaseDialogBuilder(
            R.string.session_expired_dialog_title,
            R.string.session_expired_dialog_message,
            R.string.session_expired_dialog_button) {
            findNavController().apply {
                popBackStack()
                popBackStack()
            }
            dismiss()
        }.show()
    }

    private fun createBaseDialogBuilder(titleId: Int, messageId: Int, positiveButtonId: Int, onClickListener: () -> Unit): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(titleId)
            .setMessage(messageId)
            .setCancelable(false)
            .setPositiveButton(positiveButtonId) { _: DialogInterface, _: Int -> onClickListener() }
    }

    companion object {
        private const val TIME_LIMIT_MILLIS = 5000L
        private const val TIME_STEP_MILLIS = 1000L
    }
}
