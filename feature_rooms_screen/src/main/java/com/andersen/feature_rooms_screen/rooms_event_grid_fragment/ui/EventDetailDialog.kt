package com.andersen.feature_rooms_screen.rooms_event_grid_fragment.ui

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
                    roomEvent.startDateTime,
                    roomEvent.endDateTime
                )
            eventPlannedDateUpcoming.text = roomEvent.date
            eventRoomUpcoming.text = roomEvent.room
            eventCityColourLineUpcoming.setBackgroundResource(R.color.yellow)
            nameOfBooker.text = roomEvent.userFullName
            roleOfBooker.text = roomEvent.userPosition
            bookerEmail.text = roomEvent.userEmail
            bookerSkype.text = roomEvent.userSkype
            descriptonOfEvent.text = roomEvent.description
            bookerEmail.setOnClickListener {
                showCopyPrompt(it, roomEvent.userEmail)
            }
            bookerSkype.setOnClickListener {
                if (!isSkypeClientInstalled()) {
                    goToMarket()
                } else {
                    val skypeUri = Uri.parse("skype:${roomEvent.userSkype}?chat")
                    val intent = Intent(Intent.ACTION_VIEW, skypeUri)
                    intent.component = ComponentName(SKYPE_COMPONENT_PACKAGE_NAME, SKYPE_COMPONENT_CLASS_NAME)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                    startActivity(intent)
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

    private fun goToMarket() {
        val marketUri = Uri.parse(MARKET_URI)
        val intent = Intent(Intent.ACTION_VIEW, marketUri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun showCopyPrompt(incomingView: View?, text: String) {
        val popupWindow = PopupWindow(requireActivity())
        val bindingPopup = PopoverCopyBinding.inflate(LayoutInflater.from(requireContext()))
        with(popupWindow) {
            contentView = bindingPopup.root
            isOutsideTouchable = true
            isFocusable = true
            overlapAnchor = true
            setBackgroundDrawable(null)
            bindingPopup.textCopyClick.setOnClickListener {
                saveTextToRAM(text)
                dismiss()
            }
        }

        popupWindow.showAsDropDown(
            incomingView,
            incomingView!!.width / 2,
            -incomingView.height * 2
        )
    }

    private fun saveTextToRAM(text: String) {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(COPIED_TEXT_LABEL, text))
    }

    companion object {
        private const val COPIED_TEXT_LABEL = "copied_text"
        private const val SKYPE_COMPONENT_PACKAGE_NAME ="com.skype.raider"
        private const val SKYPE_COMPONENT_CLASS_NAME = "com.skype4life.MainActivity"
        private const val MARKET_URI = "market://details?id=com.skype.raider"
    }
}
