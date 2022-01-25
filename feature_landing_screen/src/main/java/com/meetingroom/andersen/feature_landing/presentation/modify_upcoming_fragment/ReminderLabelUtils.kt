package com.meetingroom.andersen.feature_landing.presentation.modify_upcoming_fragment

import android.content.Context
import com.meetingroom.andersen.feature_landing.R

private const val MILLIS_IN_MINUTE = 60000
private const val HOUR_IN_DAY = 24
private const val MIN_IN_HOUR = 60

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

fun setReminderTime(context: Context, remainingTime: String): String {
    var time = remainingTime.toLong() / MILLIS_IN_MINUTE
    with(context.resources) {
        return if (time < MIN_IN_HOUR) "${time}${getString(R.string.shortMinute)}"
        else {
            time /= MIN_IN_HOUR
            if (time > HOUR_IN_DAY) "${time / HOUR_IN_DAY}${getString(R.string.shortDay)}"
            else "${time}${getString(R.string.shortHour)}"
        }
    }
}