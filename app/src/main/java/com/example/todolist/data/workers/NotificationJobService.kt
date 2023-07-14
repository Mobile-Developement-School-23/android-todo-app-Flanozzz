package com.example.todolist.data.workers

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todolist.R
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.data.workers.NotificationWorker.Companion.NOTIFICATION_ID
import com.example.todolist.utils.getStringByImportance
import com.example.todolist.utils.unixToMillis
import java.util.concurrent.TimeUnit


@SuppressLint("SpecifyJobSchedulerIdRange")
class NotificationJobService: JobService() {

    override fun onStartJob(params: JobParameters): Boolean {

        startForeground(NOTIFICATION_ID, createNotification("test title", "test message"))

        jobFinished(params, false)

        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return true
    }

    fun scheduleNotification(toDoItem: ToDoItem, context: Context){
        if(toDoItem.deadline == null) return

        val currentTime = System.currentTimeMillis()
        val delay = unixToMillis(toDoItem.deadline) - currentTime
        Log.d("toDoNotifications", "scheduleNotification delay - $delay")
        Log.d("toDoNotifications", "scheduleNotification deadline - ${unixToMillis(toDoItem.deadline)}")
        Log.d("toDoNotifications", "scheduleNotification currentTime - $currentTime")

        val importanceLowercaseStr = context.getString(R.string.Importance).lowercase()
        val importanceValueStr = getStringByImportance(toDoItem.importance, context).lowercase()
        val notificationMassage =
            "${toDoItem.text} | $importanceLowercaseStr - $importanceValueStr"

        val inputData = Data.Builder()
            .putString(NotificationWorker.NOTIFICATION_TITLE, context.getString(R.string.Deadline_missed))
            .putString(NotificationWorker.NOTIFICATION_MESSAGE, notificationMassage)
            .putString(NotificationWorker.INPUT_DATA_TASK_ID, toDoItem.id)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(toDoItem.id)
            .build()

        WorkManager.getInstance(context).cancelAllWorkByTag(toDoItem.id)
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    private fun showNotification(title: String?, message: String?) {
        val notificationBuilder = NotificationCompat.Builder(applicationContext, NotificationWorker.CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_list_alt_24)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as
                NotificationManager
        Log.d("toDoNotifications", "in showNotification")
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channelName = applicationContext.getString(R.string.deadline_notifications)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(NotificationWorker.CHANNEL_ID, channelName, importance)
                notificationManager.createNotificationChannel(channel)
            }
            Log.d("toDoNotifications", "show notification")
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    private fun createNotification(title: String?, message: String?): Notification {
        Log.w("toDoNotifications", "create notification")
        val notificationBuilder = NotificationCompat.Builder(applicationContext, NotificationWorker.CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_list_alt_24)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as
                NotificationManager
        Log.d("toDoNotifications", "in showNotification")
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channelName = applicationContext.getString(R.string.deadline_notifications)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(NotificationWorker.CHANNEL_ID, channelName, importance)
                notificationManager.createNotificationChannel(channel)
            }
            Log.d("toDoNotifications", "show notification")
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
        return notificationBuilder.build()
    }

    companion object{
        private const val NOTIFICATION_JOB_ID = 1

        fun testSchedule(context: Context){
            Log.w("toDoNotifications", "testSchedule")
            val serviceComponent = ComponentName(context, NotificationJobService::class.java)
            val builder = JobInfo.Builder(NOTIFICATION_JOB_ID, serviceComponent)
            builder.setRequiresDeviceIdle(true) // Дополнительные настройки в зависимости от ваших требований
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
            builder.setMinimumLatency(10000)
            builder.setOverrideDeadline(10000)

            val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(builder.build())

        }
    }
}