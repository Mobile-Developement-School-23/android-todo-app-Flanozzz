package com.example.todolist.ui.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
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

// Я пытался сделать уведомления с помощью work manager, но как-то не получилось(
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
        val title = inputData.getString(NOTIFICATION_TITLE) ?: return Result.failure()
        val message = inputData.getString(NOTIFICATION_MESSAGE) ?: return Result.failure()
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

    private fun showNotification(title: String, message: String) {
        val notificationBuilder = buildNotification(title, message)

        val notificationManager = getNotificationManager()
        Log.d("toDoNotifications", "in showNotification")
        if (checkSelfPermission()) {
            val channel = buildNotificationChanel()
            notificationManager.createNotificationChannel(channel)
            Log.d("toDoNotifications", "show notification")
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    private fun buildNotificationChanel(): NotificationChannel{
        val channelName = context.getString(R.string.deadline_notifications)
        val importance = NotificationManager.IMPORTANCE_HIGH
        return NotificationChannel(CHANNEL_ID, channelName, importance)
    }

    private fun checkSelfPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }

    private fun getNotificationManager(): NotificationManager{
        return context.getSystemService(NOTIFICATION_SERVICE) as
                NotificationManager
    }

    private fun buildNotification(title: String, message: String): NotificationCompat.Builder{
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_list_alt_24)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    companion object {
        private const val CHANNEL_ID = "ToDoChannel"
        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_TITLE = "notificationTitle"
        private const val NOTIFICATION_MESSAGE = "notificationMessage"
        private const val INPUT_DATA_TASK_ID = "taskId"
    }
}