package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.model

sealed class UserTimeTypes(val id: String) {
    class Never : UserTimeTypes("Never")
    class Custom : UserTimeTypes("Custom...")

    companion object {
        fun fromId(id: String): UserTimeTypes {
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
