package com.example.todolist

import android.app.Application
import com.example.todolist.di.Repositories
import com.example.todolist.data.DataSyncWorker

class ToDoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Repositories.init(this)
        DataSyncWorker.startPeriodicWork(this)
    }
}