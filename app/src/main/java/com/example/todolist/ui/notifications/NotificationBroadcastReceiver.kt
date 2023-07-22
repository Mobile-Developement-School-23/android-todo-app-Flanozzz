package com.example.todolist.ui.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.ListenableWorker
import com.example.todolist.R
import com.example.todolist.ToDoApp
import com.example.todolist.data.repository.ToDoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationBroadcastReceiver: BroadcastReceiver() {

    @Inject
    lateinit var repository: ToDoRepository

    override fun onReceive(context: Context, intent: Intent) {

        (context.applicationContext as ToDoApp)
            .appComponent
            .inject(this)

        val title = intent.getStringExtra(NOTIFICATION_TITLE)
        val message = intent.getStringExtra(NOTIFICATION_MESSAGE)
        val toDoItemId = intent.getStringExtra(INPUT_DATA_TASK_ID)
        Log.w("toDoNotifications", "onReceive $title $message")
        if(title == null || message == null|| toDoItemId == null) return

        CoroutineScope(Dispatchers.IO).launch {
            val toDoItem = repository.getById(toDoItemId) ?: return@launch

            if(toDoItem.isDone){
                Log.d("toDoNotifications", "task ${toDoItem.id} is done - notification skip")
                return@launch
            }
            Log.d("toDoNotifications", "task id on notify - $toDoItemId")

            showNotification(title, message, context)
        }
    }

    private fun showNotification(title: String, message: String, context: Context) {
        val notificationBuilder = buildNotification(title, message, context)

        val notificationManager = getNotificationManager(context)
        Log.d("toDoNotifications", "in showNotification")
        if (checkSelfPermission(context)) {
            val channel = buildNotificationChanel(context)
            notificationManager.createNotificationChannel(channel)
            Log.d("toDoNotifications", "show notification")
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    private fun buildNotificationChanel(context: Context): NotificationChannel {
        val channelName = context.getString(R.string.deadline_notifications)
        val importance = NotificationManager.IMPORTANCE_HIGH
        return NotificationChannel(CHANNEL_ID, channelName, importance)
    }

    private fun checkSelfPermission(context: Context): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }

    private fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
    }

    private fun buildNotification(title: String, message: String, context: Context): NotificationCompat.Builder{
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_list_alt_24)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    companion object {
        const val CHANNEL_ID = "ToDoChannel"
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_TITLE = "notificationTitle"
        const val NOTIFICATION_MESSAGE = "notificationMessage"
        const val INPUT_DATA_TASK_ID = "taskId"
    }
}