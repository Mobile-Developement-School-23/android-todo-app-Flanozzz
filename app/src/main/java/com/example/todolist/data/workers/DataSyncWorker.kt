package com.example.todolist.data.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.todolist.ToDoApp
import com.example.todolist.data.repository.IRepository
import com.example.todolist.di.scopes.AppScope
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AppScope
class DataSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) :
    CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var repository: IRepository

    override suspend fun doWork(): Result {
        repository.syncData()
        return Result.success()
    }

    init {
        (context as ToDoApp)
            .appComponent
            .inject(this)
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