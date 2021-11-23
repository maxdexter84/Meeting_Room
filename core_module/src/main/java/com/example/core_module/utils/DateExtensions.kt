package com.example.core_module.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun LocalDate.dateToString(format: String) = this.format(DateTimeFormatter.ofPattern(format, Locale.UK))

fun String.stringToDate(format: String) = LocalDate.parse(this, DateTimeFormatter.ofPattern(format, Locale.UK))

