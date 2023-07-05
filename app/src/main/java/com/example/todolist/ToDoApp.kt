package com.example.todolist

import android.app.Application
import com.example.todolist.data.DataSyncWorker
import com.example.todolist.di.AppComponent
import com.example.todolist.di.AppModule
import com.example.todolist.di.DaggerAppComponent
import com.example.todolist.di.NetworkModule
import com.example.todolist.di.RepositoryModule

class ToDoApp : Application() {

    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        DataSyncWorker.startPeriodicWork(this)

        appComponent = DaggerAppComponent
            .builder()
            .withContext(this)
            .build()
    }
}