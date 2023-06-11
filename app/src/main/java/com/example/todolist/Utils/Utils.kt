package com.example.todolist.Utils

import java.text.SimpleDateFormat
import java.util.*

fun getUnixTime(dayOfMonth: Int, month: Int, year: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, dayOfMonth, 0, 0, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis / 1000
}

fun getFormattedDate(unixTime: Long): String {
    val locale = Locale.getDefault()
    val dateFormat = SimpleDateFormat("d MMMM yyyy", locale)
    val date = Date(unixTime * 1000)
    return dateFormat.format(date)
}
