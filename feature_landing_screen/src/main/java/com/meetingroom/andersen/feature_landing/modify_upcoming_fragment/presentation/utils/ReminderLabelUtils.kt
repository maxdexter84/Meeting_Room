package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.utils

import android.content.Context
import androidx.core.text.isDigitsOnly
import com.meetingroom.andersen.feature_landing.R

fun getShortReminderLabel(context: Context, longLabel: String): String {
    with (context.getString(R.string.reminder_disabled_text_for_time)) {
        if (longLabel == this) return this
    }
    val parts = longLabel.split(" ")
    if (parts.size != 3 && parts[0].isDigitsOnly().not() && parts[2] != context.getString(R.string.before) ) {
        return ""
    } else {
        var shortLabel = parts[0]
        with (context.resources) {
            shortLabel += when (parts[1]) {
                in getStringArray(R.array.arrayMinutes) -> getString(R.string.shortMinute)
                in getStringArray(R.array.arrayHours) -> getString(R.string.shortHour)
                in getStringArray(R.array.arrayDays) -> getString(R.string.shortDay)
                else -> return ""
            }
        }
        return shortLabel
    }
}

fun getLongReminderLabel(context: Context, shortLabel: String): String {
    with (context.getString(R.string.reminder_disabled_text_for_time)) {
        if (shortLabel == this) return this
    }
    val number = shortLabel.filter { it.isDigit() }.toInt()
    with (context.resources) {
        val timeType = when (shortLabel.last().toString()) {
            getString(R.string.shortMinute) -> getQuantityString(R.plurals.amountOfMinutes, number)
            getString(R.string.shortHour) -> getQuantityString(R.plurals.amountOfHours, number)
            getString(R.string.shortDay) -> getQuantityString(R.plurals.amountOfDays, number)
            else -> return ""
        }
        return String.format(getString(R.string.user_selected_custom_time_option), number, timeType)
    }
}