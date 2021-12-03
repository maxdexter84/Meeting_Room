package com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.model

import com.meetingroom.ui.R


sealed class UserTimeTypes(val id: Int) {
    class Never : UserTimeTypes(R.string.reminder_disabled_text_for_time)
    class Custom : UserTimeTypes(R.string.reminder_custom_text_for_time)

    companion object {
        fun fromId(id: Int): UserTimeTypes {
            for (type in values()) {
                if (type.id == id) return type
            }
            throw IllegalArgumentException()
        }

        private fun values(): Array<UserTimeTypes> {
            return arrayOf(Never(), Custom())
        }
    }
}
