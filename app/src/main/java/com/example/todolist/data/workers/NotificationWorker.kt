package com.example.todolist.data.workers

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.ui.text.toLowerCase
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.todolist.R
import com.example.todolist.ToDoApp
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.data.repository.IRepository
import com.example.todolist.utils.getStringByImportance
import com.example.todolist.utils.unixToMillis
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @Inject
    lateinit var repository: IRepository

    init {
        (context as ToDoApp)
            .appComponent
            .inject(this)
    }

    override suspend fun doWork(): Result {
        val title = inputData.getString(NOTIFICATION_TITLE)
        val message = inputData.getString(NOTIFICATION_MESSAGE)
        val toDoItemId = inputData.getString(INPUT_DATA_TASK_ID) ?: return Result.failure()

        val toDoItem = repository.getById(toDoItemId) ?: return Result.failure()

        if(toDoItem.isDone){
            Log.d("toDoNotifications", "task ${toDoItem.id} is done - notification skip")
            return Result.success()
        }
        Log.d("toDoNotifications", "task id on notify - $toDoItemId")
        showNotification(title, message)

        return Result.success()
    }

    private fun showNotification(title: String?, message: String?) {
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_list_alt_24)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as
                NotificationManager
        Log.d("toDoNotifications", "in showNotification")
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channelName = context.getString(R.string.deadline_notifications)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
                notificationManager.createNotificationChannel(channel)
            }
            Log.d("toDoNotifications", "show notification")
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    companion object {
        const val CHANNEL_ID = "ToDoChannel"
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_TITLE = "notificationTitle"
        const val NOTIFICATION_MESSAGE = "notificationMessage"
        const val INPUT_DATA_TASK_ID = "taskId"

        fun scheduleNotification(toDoItem: ToDoItem, context: Context){
            if(toDoItem.deadline == null) return

            val currentTime = System.currentTimeMillis()
            val delay = unixToMillis(toDoItem.deadline) - currentTime
            Log.d("toDoNotifications", "scheduleNotification delay - $delay")
            if (delay < 0) return
            Log.d("toDoNotifications", "scheduleNotification deadline - ${unixToMillis(toDoItem.deadline)}")
            Log.d("toDoNotifications", "scheduleNotification currentTime - $currentTime")

            val importanceLowercaseStr = context.getString(R.string.Importance).lowercase()
            val importanceValueStr = getStringByImportance(toDoItem.importance, context).lowercase()
            val notificationMassage =
                "${toDoItem.text} | $importanceLowercaseStr - $importanceValueStr"

            val inputData = Data.Builder()
                .putString(NOTIFICATION_TITLE, context.getString(R.string.Deadline_missed))
                .putString(NOTIFICATION_MESSAGE, notificationMassage)
                .putString(INPUT_DATA_TASK_ID, toDoItem.id)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag(toDoItem.id)
                .build()

            WorkManager.getInstance(context).cancelAllWorkByTag(toDoItem.id)
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}