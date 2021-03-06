package com.andersen.feature_rooms_screen.presentation.rooms_event_grid

import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.navigation.fragment.navArgs
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.DialogEventDetailBinding
import com.meetingroom.andersen.feature_rooms_screen.databinding.PopoverCopyBinding
import android.net.Uri
import android.content.pm.PackageManager
import android.content.Intent
import com.example.core_module.utils.dateToString
import com.example.core_module.utils.stringToDate

class EventDetailDialog: BaseDialogFragment<DialogEventDetailBinding>(DialogEventDetailBinding::inflate) {

    private val args: EventDetailDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val roomEvent = args.roomEvent
        with (binding) {
            eventTitleUpcoming.text = roomEvent.title
            eventPlannedTimeUpcoming.text =
                String.format(
                    binding.root.resources.getString(R.string.event_time),
                    getTime(roomEvent.startDateTime),
                    getTime(roomEvent.endDateTime)
                )
            eventPlannedDateUpcoming.text = roomEvent.date.stringToDate(INPUT_DATE_FORMAT)
                .dateToString(OUTPUT_DATE_FORMAT)
            eventRoomUpcoming.text = roomEvent.room
            eventCityColourLineUpcoming.setBackgroundColor(roomEvent.colorRoom)
            nameOfBooker.text = roomEvent.userFullName
            roleOfBooker.text = roomEvent.userPosition
            bookerEmail.text = roomEvent.userEmail
            bookerSkype.text = roomEvent.userSkype
            descriptonOfEvent.text = roomEvent.description
            bookerEmail.setOnClickListener {
                showCopyPrompt(it, roomEvent.userEmail)
            }
            bookerSkype.setOnClickListener {
                if (isSkypeClientInstalled().not()) {
                    goToMarket()
                } else {
                    startActivity(Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse("skype:${roomEvent.userSkype}?chat")
                        component = ComponentName(SKYPE_COMPONENT_PACKAGE_NAME, SKYPE_COMPONENT_CLASS_NAME)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }
            }
        }
    }

    private fun isSkypeClientInstalled(): Boolean {
        val packageManager = requireContext().packageManager
        try {
            packageManager.getPackageInfo(SKYPE_COMPONENT_PACKAGE_NAME, PackageManager.GET_ACTIVITIES)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
        return true
    }

    private fun goToMarket() = startActivity(Intent().apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse(MARKET_URI)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })

    private fun showCopyPrompt(incomingView: View?, text: String) = PopupWindow(requireActivity()).apply {
        val bindingPopup = PopoverCopyBinding.inflate(LayoutInflater.from(requireContext()))
        contentView = bindingPopup.root
        isOutsideTouchable = true
        isFocusable = true
        overlapAnchor = true
        setBackgroundDrawable(null)
        bindingPopup.textCopyClick.setOnClickListener {
            saveTextToRAM(text)
            dismiss()
        }
        showAsDropDown(
            incomingView,
            incomingView!!.width / 2,
            -incomingView.height * 2
        )
    }

    private fun saveTextToRAM(text: String) = (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply {
            setPrimaryClip(ClipData.newPlainText(COPIED_TEXT_LABEL, text))
    }

    private fun getTime(stringDateAndTime: String): String {
        val strings = stringDateAndTime.split("T")
        return strings[1].substring(START_INDEX_FOR_TIME, END_INDEX_FOR_TIME)
    }

    companion object {
        private const val COPIED_TEXT_LABEL = "copied_text"
        private const val SKYPE_COMPONENT_PACKAGE_NAME = "com.skype.raider"
        private const val SKYPE_COMPONENT_CLASS_NAME = "com.skype4life.MainActivity"
        private const val MARKET_URI = "market://details?id=com.skype.raider"
        private const val START_INDEX_FOR_TIME = 0
        private const val END_INDEX_FOR_TIME = 5
        private const val INPUT_DATE_FORMAT = "yyyy-MM-d"
        private const val OUTPUT_DATE_FORMAT = "d MMM YYYY"
    }
}
