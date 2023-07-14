package com.example.todolist.data.workers

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContentScope
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.impl.utils.ForceStopRunnable.BroadcastReceiver
import com.example.todolist.R
import com.example.todolist.ToDoApp
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.data.repository.IRepository
import com.example.todolist.utils.getStringByImportance
import com.example.todolist.utils.unixToMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.coroutineContext


@SuppressLint("RestrictedApi")
class NotificationBroadcastReceiver: BroadcastReceiver() {

    @Inject
    lateinit var repository: IRepository

    override fun onReceive(context: Context, intent: Intent?) {

        Log.w("toDoNotifications", "onReceive")

        (context.applicationContext as ToDoApp)
            .appComponent
            .inject(this)


        val title = intent?.getStringExtra(NOTIFICATION_TITLE) ?: return
        Log.w("toDoNotifications", "1")
        val message = intent.getStringExtra(NOTIFICATION_MESSAGE) ?: return
        Log.w("toDoNotifications", "2")
        val toDoItemId = intent.getStringExtra(INPUT_DATA_TASK_ID) ?: return
        Log.w("toDoNotifications", "params is get")
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
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
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_list_alt_24)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as
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

    companion object{
        private const val NOTIFICATION_REQUEST_CODE = 1
        const val CHANNEL_ID = "ToDoChannel"
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_TITLE = "notificationTitle"
        const val NOTIFICATION_MESSAGE = "notificationMessage"
        const val INPUT_DATA_TASK_ID = "taskId"

//        fun testSchedule(context: Context){
//            Log.w("toDoNotifications", "testSchedule")
//            // Получение системного сервиса AlarmManager
//            // Получение системного сервиса AlarmManager
//            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
//
//// Создание Intent для запуска вашего BroadcastReceiver
//
//// Создание Intent для запуска вашего BroadcastReceiver
//            val intent = Intent(context, NotificationBroadcastReceiver::class.java)
//            intent.putExtra("message", "Ваше уведомление")
//
//// Создание PendingIntent, который будет использован при срабатывании уведомления
//
//// Создание PendingIntent, который будет использован при срабатывании уведомления
//            val pendingIntent =
//                PendingIntent.getBroadcast(context, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE)
//            val calendar: Calendar = Calendar.getInstance()
//            calendar.timeInMillis = System.currentTimeMillis()
//            calendar.add(
//                Calendar.SECOND,
//                10
//            ) // Пример: уведомление будет запланировано через 10 секунд
//
//
//// Установка уведомления с помощью AlarmManager
//
//// Установка уведомления с помощью AlarmManager
//            alarmManager!!.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                pendingIntent
//            )
//            Log.w("toDoNotifications", "set exact")
//        }

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

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

// Создание Intent для запуска вашего BroadcastReceiver

// Создание Intent для запуска вашего BroadcastReceiver
            val intent = Intent(context, NotificationBroadcastReceiver::class.java)
            intent.putExtra(NOTIFICATION_MESSAGE, notificationMassage)
            intent.putExtra(NOTIFICATION_TITLE, context.getString(R.string.Deadline_missed))
            intent.putExtra(INPUT_DATA_TASK_ID, toDoItem.id)

            val pendingIntent =
                PendingIntent.getBroadcast(context, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE)

            val time = System.currentTimeMillis() + delay
            alarmManager!!.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
            Log.w("toDoNotifications", "set exact $time")
        }
    }
}