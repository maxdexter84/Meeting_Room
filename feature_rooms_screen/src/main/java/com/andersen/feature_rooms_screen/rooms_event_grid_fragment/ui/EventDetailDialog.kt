package com.andersen.feature_rooms_screen.rooms_event_grid_fragment.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.DialogEventDetailBinding


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
                // add on click listener
            }
            bookerSkype.setOnClickListener {
                // add on click listener
            }
        }
    }
}