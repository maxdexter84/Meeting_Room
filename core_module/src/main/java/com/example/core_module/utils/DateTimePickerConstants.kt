package com.example.core_module.utils

object DateTimePickerConstants {
    const val INPUT_DATE_FORMAT = "yyyy-MM-d"
    const val OUTPUT_DATE_FORMAT = "EEE, d MMM"
    const val MINUTE_TO_ROUND = 5
    const val MAX_MONTH = 3L
    const val DEFAULT_HOURS_EVENT_LENGTH = 1L
    const val MONTH_VALUE_OFFSET = 1
    enum class TimePickerTag {
        START, END
    }

    const val EVENT_INACTIVITY_LIMIT = 30000L
    const val LOCK_INACTIVITY_LIMIT = 120000L
}