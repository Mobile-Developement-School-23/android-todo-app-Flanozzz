package com.example.todolist.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.TypedValue
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.todolist.R
import com.example.todolist.data.model.ToDoItem
import com.google.android.material.snackbar.Snackbar
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*


fun getUnixTime(dayOfMonth: Int, monthIndex: Int, year: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(year, monthIndex, dayOfMonth, 0, 0, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return millisToUnix(calendar.timeInMillis)
}

fun unixTimeToDMY(unixTime: Long): String {
    val locale = Locale.getDefault()
    val dateFormat = SimpleDateFormat("d MMMM yyyy", locale)
    val date = Date(unixToMillis(unixTime))
    return dateFormat.format(date)
}

fun unixTimeToHHMM(unixTime: Long): String {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val date = Date(unixToMillis(unixTime))
    return dateFormat.format(date)
}

fun getNextDayUnixTime(): Long {
    val tomorrow = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1)
    val midnight = LocalDateTime.of(tomorrow, LocalTime.MIDNIGHT)
    return millisToUnix(midnight.toMillis())
}

fun setUnixTime(hour: Int, minute: Int, unixTime: Long): Long {
    val localDateTime = Instant.ofEpochSecond(unixTime).atZone(ZoneId.systemDefault()).toLocalDateTime()
    val updatedDateTime = localDateTime.withHour(hour).withMinute(minute).withSecond(0)
    return millisToUnix(updatedDateTime.toMillis())
}

fun getEndOfDayUnixTime(unixTime: Long): Long {
    val dateTime = Instant.ofEpochSecond(unixTime).atZone(ZoneId.systemDefault()).toLocalDateTime()
    val endOfDay = dateTime.with(LocalTime.MAX)
    return millisToUnix(endOfDay.toMillis())
}

fun getAndroidAttrTextColor(context: Context, colorId: Int): Int {
    val typedValue = TypedValue()
    val theme = context.theme

    theme.resolveAttribute(colorId, typedValue, true)

    val textColorResId = typedValue.resourceId
    return ContextCompat.getColor(context, textColorResId)
}

fun getCurrentUnixTime(): Long{
    return millisToUnix(System.currentTimeMillis())
}

fun unixToMillis(unix: Long): Long{
    return unix * 1000
}

fun millisToUnix(millis: Long): Long{
    return millis / 1000
}

@SuppressLint("HardwareIds")
fun getDeviceId(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

fun makeRefreshSnackbar(container: ViewGroup, action: () -> Unit): Snackbar {
    val snackbar = Snackbar.make(container, container.context.getString(R.string.retry), Snackbar.LENGTH_LONG)
    snackbar.setAction(container.context.getString(R.string.refresh)) {
        action()
    }
    return snackbar
}

fun setCurrentDateToDatePicker(datePicker: DatePicker){
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    datePicker.updateDate(year, month, day)
}

fun hideKeyboard(activity: Activity){
    val view: View? = activity.currentFocus
    if(view != null){
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun setTextViewByImportance(textView: TextView, importance: ToDoItem.Importance, context: Context){
    when(importance){
        ToDoItem.Importance.DEFAULT -> textView.text = context.getString(R.string.No)
        ToDoItem.Importance.LOW -> textView.text = context.getString(R.string.Low)
        ToDoItem.Importance.HIGH -> textView.text = context.getString(R.string.High)
    }
}

fun LocalDateTime.toMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun getStringByImportance(importance: ToDoItem.Importance, context: Context): String{
    return when(importance){
        ToDoItem.Importance.DEFAULT -> context.getString(R.string.No)
        ToDoItem.Importance.LOW -> context.getString(R.string.Low)
        ToDoItem.Importance.HIGH -> context.getString(R.string.High)
    }
}

fun setSettingsTheme(sharedPreferences: SharedPreferences){
    val mode = when(sharedPreferences.getString(Constants.THEME, Constants.SYSTEM_THEME)){
        Constants.LIGHT_THEME -> AppCompatDelegate.MODE_NIGHT_NO
        Constants.DARK_THEME -> AppCompatDelegate.MODE_NIGHT_YES
        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
    AppCompatDelegate.setDefaultNightMode(mode)
}