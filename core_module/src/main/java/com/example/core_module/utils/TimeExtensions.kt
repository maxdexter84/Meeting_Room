package com.example.core_module.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.math.ceil

private const val MIN = "min"
private const val HOUR = "hour"
private const val HOURS = "hours"

fun LocalTime.timeToString(format: String): String {
    val formatter = DateTimeFormatter.ofPattern(format)
    return this.format(formatter)
}

fun String.stringToTime(format: String): LocalTime {
    val formatter = DateTimeFormatter.ofPattern(format)
    return LocalTime.parse(this, formatter)
}

fun String.stringToDateTime(format: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern(format)
    return LocalDateTime.parse(this, formatter)
}

fun String.stringTimeToStringDateTime(inputFormat: String, outputFormat: String): String {
    val outputFormatter = DateTimeFormatter.ofPattern(outputFormat)

    return LocalDateTime.of(LocalDate.now(ZoneOffset.UTC), this.stringToTime(inputFormat))
        .format(outputFormatter)
}

fun LocalTime.roundUpMinute(min: Int): LocalTime {
    val newMinute = (ceil(minute / min.toDouble()) * min).toInt()
    if (newMinute == 60) {
        return plusHours(1).withMinute(0)
    }
    return withMinute(newMinute)
}

fun Int.minutesToTimeString(): String {
    val hours = TimeUnit.MINUTES.toHours(this.toLong()).toInt()
    val remainMinutes = this - TimeUnit.HOURS.toMinutes(hours.toLong()).toInt()
    var timeString = "$this $MIN"

    if (hours > 0) {
        timeString = if (hours == 1) {
            "$hours $HOUR"
        } else {
            "$hours $HOURS"
        }
        if (remainMinutes != 0) {
            timeString += " $remainMinutes $MIN"
        }
    }
    return timeString
}