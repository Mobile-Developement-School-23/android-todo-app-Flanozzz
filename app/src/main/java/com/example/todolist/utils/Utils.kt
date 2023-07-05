package com.example.todolist.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import com.example.todolist.R
import com.example.todolist.data.model.ToDoItem
import com.google.android.material.snackbar.Snackbar
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

fun getAndroidAttrTextColor(context: Context, colorId: Int): Int {
    val typedValue = TypedValue()
    val theme = context.theme

    theme.resolveAttribute(colorId, typedValue, true)

    val textColorResId = typedValue.resourceId
    return ContextCompat.getColor(context, textColorResId)
}

fun getCurrentUnixTime(): Long{
    return System.currentTimeMillis() / 1000
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