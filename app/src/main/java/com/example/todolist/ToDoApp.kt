package com.example.todolist

import android.app.Application
import android.util.Log
import com.example.todolist.data.DataSyncWorker
import com.example.todolist.di.app.AppComponent
import com.example.todolist.di.app.DaggerAppComponent

class ToDoApp : Application() {

    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        DataSyncWorker.startPeriodicWork(this)
        Log.w("AAA", "create")
        appComponent = DaggerAppComponent
            .factory()
            .create(this)
    }
}