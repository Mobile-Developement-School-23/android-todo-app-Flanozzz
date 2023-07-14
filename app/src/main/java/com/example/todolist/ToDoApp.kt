package com.example.todolist

import android.app.Application
import android.content.Context
import com.example.todolist.data.DataSyncWorker
import com.example.todolist.di.app.AppComponent
import com.example.todolist.di.app.DaggerAppComponent
import com.example.todolist.utils.Constants.SHARED_PREFERENCES_NAME
import com.example.todolist.utils.setSettingsTheme

class ToDoApp : Application() {

    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        DataSyncWorker.startPeriodicWork(this)

        appComponent = DaggerAppComponent
            .factory()
            .create(this)

        val settings = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        setSettingsTheme(settings)
    }
}