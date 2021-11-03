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

