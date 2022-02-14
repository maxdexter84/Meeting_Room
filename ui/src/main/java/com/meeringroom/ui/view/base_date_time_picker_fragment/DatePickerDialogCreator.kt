package com.meeringroom.ui.view.base_date_time_picker_fragment

import android.app.DatePickerDialog
import android.content.Context
import com.example.core_module.utils.DateTimePickerConstants.MAX_MONTH
import com.example.core_module.utils.DateTimePickerConstants.MONTH_VALUE_OFFSET
import com.example.core_module.utils.DateTimePickerUtils.getLocalDateTime
import com.meetingroom.ui.R
import java.time.LocalDate
import java.util.*

class DatePickerDialogCreator(
    val context: Context,
    val date: LocalDate,
    private val onDateSetListener: DatePickerDialog.OnDateSetListener
) {
    fun create(): DatePickerDialog {
        return DatePickerDialog(
            context,
            onDateSetListener,
            date.year,
            date.monthValue - MONTH_VALUE_OFFSET,
            date.dayOfMonth
        ).apply {
            val minDate = LocalDate.now()
            val maxDate = LocalDate.now().plusMonths(MAX_MONTH)
            setButton(DatePickerDialog.BUTTON_POSITIVE, context.getString(R.string.ok_button), this)
            setButton(
                DatePickerDialog.BUTTON_NEGATIVE,
                context.getString(R.string.cancel_button),
                this
            )
            datePicker.init(
                date.year,
                date.monthValue - MONTH_VALUE_OFFSET,
                date.dayOfMonth
            ) { datePicker, year, month, day ->
                val localDate = LocalDate.of(year, month + MONTH_VALUE_OFFSET, day)
                when {
                    localDate.isBefore(minDate) -> datePicker.updateDate(
                        minDate.year,
                        minDate.monthValue - 1,
                        minDate.dayOfMonth
                    )
                    localDate.isAfter(maxDate) -> datePicker.updateDate(
                        maxDate.year,
                        maxDate.monthValue - 1,
                        maxDate.dayOfMonth
                    )
                }
            }
            datePicker.minDate = getLocalDateTime(minDate)
            datePicker.maxDate = getLocalDateTime(maxDate)
            datePicker.firstDayOfWeek = Calendar.MONDAY
            setCancelable(false)
        }
    }
}