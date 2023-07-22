package com.example.todolist.ui.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.todolist.R
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.ui.notifications.NotificationBroadcastReceiver.Companion.INPUT_DATA_TASK_ID
import com.example.todolist.ui.notifications.NotificationBroadcastReceiver.Companion.NOTIFICATION_MESSAGE
import com.example.todolist.ui.notifications.NotificationBroadcastReceiver.Companion.NOTIFICATION_TITLE
import com.example.todolist.utils.getStringByImportance
import com.example.todolist.utils.unixToMillis
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationScheduler @Inject constructor(
    private val context: Context
) {
    fun scheduleNotification(toDoItem: ToDoItem){
        if(toDoItem.deadline == null) return

        val delay = calculateDelay(toDoItem.deadline)
        if (delay < 0) return

        val notificationMassage = buildNotificationMessage(toDoItem)

        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        intent.putExtra(NOTIFICATION_TITLE, context.getString(R.string.Deadline_missed))
        intent.putExtra(NOTIFICATION_MESSAGE, notificationMassage)
        intent.putExtra(INPUT_DATA_TASK_ID, toDoItem.id)

        val requestCode = toDoItem.id.hashCode() and 0x7FFFFFFF
        Log.d("toDoNotifications", "request code - $requestCode")
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = unixToMillis(toDoItem.deadline)
        //val time = System.currentTimeMillis() + 20000

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        Log.w("toDoNotifications", "setExactAndAllowWhileIdle notification time - $time")

//        val inputData = buildInputData(notificationMassage, toDoItem)
//        val workRequest = buildWorkRequest(delay, inputData, toDoItem)
//        performWorkWithCancellationAndEnqueue(toDoItem, workRequest)
    }

    private fun calculateDelay(toDoItemDeadline: Long): Long{
        val currentTime = System.currentTimeMillis()
        val delay = unixToMillis(toDoItemDeadline) - currentTime
        Log.d("toDoNotifications", "scheduleNotification delay - $delay")
        Log.d("toDoNotifications", "scheduleNotification deadline - ${unixToMillis(toDoItemDeadline)}")
        Log.d("toDoNotifications", "scheduleNotification currentTime - $currentTime")
        return delay
    }

    //эти функции относятсья к notification worker, пока не стал их удалять
    private fun performWorkWithCancellationAndEnqueue(
        toDoItem: ToDoItem,
        workRequest: WorkRequest
    ) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWorkByTag(toDoItem.id)
        workManager.enqueue(workRequest)
    }

    private fun buildNotificationMessage(toDoItem: ToDoItem): String{
        val importanceLowercaseStr = context.getString(R.string.Importance).lowercase()
        val importanceValueStr = getStringByImportance(toDoItem.importance, context).lowercase()
        return "${toDoItem.text} | $importanceLowercaseStr - $importanceValueStr"
    }

    private fun buildInputData(
        notificationMassage: String,
        toDoItem: ToDoItem
    ): Data{
        return Data.Builder()
            .putString(NOTIFICATION_TITLE, context.getString(R.string.Deadline_missed))
            .putString(NOTIFICATION_MESSAGE, notificationMassage)
            .putString(INPUT_DATA_TASK_ID, toDoItem.id)
            .build()
    }

    private fun buildWorkRequest(
        delay: Long,
        inputData: Data,
        toDoItem: ToDoItem
    ): OneTimeWorkRequest{
        return OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(toDoItem.id)
            .build()
    }
}