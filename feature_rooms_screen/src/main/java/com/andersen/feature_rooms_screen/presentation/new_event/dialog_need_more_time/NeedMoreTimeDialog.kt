package com.andersen.feature_rooms_screen.presentation.new_event.dialog_need_more_time

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.CustomDialogNeedMoreTimeBinding


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
                secondsTextView.text = (millisUntilFinished / TIME_STEP_MILLIS + 1).toString()
            }
            override fun onFinish() {
                context?.let {
                    showExpiredSessionDialog()
                }
            }
        }.start()

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.need_more_time_dialog_title)
            .setMessage(R.string.need_more_time_dialog_message)
            .setView(dialogView)
            .setCancelable(false)
            .setPositiveButton(R.string.need_more_time_dialog_button) { _: DialogInterface, _: Int -> dismiss() }
            .create()
    }

    private fun showExpiredSessionDialog() {
        dialog?.hide()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.session_expired_dialog_title)
            .setMessage(R.string.session_expired_dialog_message)
            .setCancelable(false)
            .setPositiveButton(R.string.session_expired_dialog_button) { _: DialogInterface, _: Int ->
                findNavController().navigate(R.id.action_to_roomsFragment)
            }
            .show()
    }

    companion object {
        private const val TIME_LIMIT_MILLIS = 15000L
        private const val TIME_STEP_MILLIS = 1000L
    }
}
