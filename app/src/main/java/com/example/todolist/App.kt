package com.example.todolist

import android.app.Application
import com.example.todolist.Repository.Repositories

class App : Application() {
    //val toDoItemsRepository = LocalToDoItemsRepository()
    //private lateinit var toDoDbItemsRepository: LocalDbRepository

    override fun onCreate() {
        super.onCreate()
        Repositories.init(context = this)
    }
}