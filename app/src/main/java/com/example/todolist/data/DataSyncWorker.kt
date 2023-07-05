package com.example.todolist.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.todolist.data.repository.ToDoRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DataSyncWorker @Inject constructor(
    context: Context,
    workerParams: WorkerParameters
) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        //repository.syncData()
        return Result.success()
    }

    companion object {
        fun startPeriodicWork(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val syncDataRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(
                repeatInterval = 8,
                repeatIntervalTimeUnit = TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueue(syncDataRequest)
        }
    }
}