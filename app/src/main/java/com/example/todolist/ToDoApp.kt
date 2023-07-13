package com.example.todolist

import android.app.Application
import android.content.Context
import com.example.todolist.data.workers.DataSyncWorker
import com.example.todolist.di.app.AppComponent
import com.example.todolist.di.app.DaggerAppComponent
import com.example.todolist.utils.Constants.SETTINGS
import com.example.todolist.utils.setSettingsTheme

class ToDoApp : Application() {

    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        DataSyncWorker.startPeriodicWork(this)

        appComponent = DaggerAppComponent
            .factory()
            .create(this)

        val settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        setSettingsTheme(settings)
    }
}