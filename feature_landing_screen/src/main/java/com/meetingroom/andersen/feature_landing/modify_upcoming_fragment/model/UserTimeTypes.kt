package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.model

import com.meetingroom.andersen.feature_landing.R

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
