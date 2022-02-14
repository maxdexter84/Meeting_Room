package com.example.core_module.utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

object DateTimePickerUtils {

    fun getLocalDateTime(date: LocalDate) =
        LocalDateTime.of(date, LocalTime.of(0, 0, 0)).atZone(ZoneId.systemDefault()).toInstant()
            .toEpochMilli()

    fun stringDateAndTimeToMillis(date: String, time: String): Long {
        val dateSegment = date.split("-")
        val timeSegment = time.split(":")
        val dateConstruct = kotlinx.datetime.LocalDateTime(
            dateSegment[0].toInt(),
            dateSegment[1].toInt(),
            dateSegment[2].toInt(),
            timeSegment[0].toInt(),
            timeSegment[1].toInt(),
        )
        return dateConstruct.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }
}