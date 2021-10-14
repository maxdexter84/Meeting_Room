package com.example.core_module.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
fun LocalDate.dateToString(format: String): String {
    val formatter = DateTimeFormatter.ofPattern(format)
    return this.format(formatter)
}

@SuppressLint("NewApi")
fun String.stringToDate(format: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(format)
    return LocalDate.parse(this, formatter)
}

@SuppressLint("NewApi")
fun String.stringDateAndTimeToMillis(time: String): Long {
    val dateSegment = this.split("-")
    val timeSegment = time.split(":")
    val dateConstruct = LocalDateTime(
        dateSegment[0].toInt(),
        dateSegment[1].toInt(),
        dateSegment[2].toInt(),
        timeSegment[0].toInt(),
        timeSegment[1].toInt(),
    )
    return dateConstruct.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}
