package com.example.todolist.repositories

import android.content.Context
import com.example.todolist.models.db.ToDoDatabase
import com.example.todolist.api.RetrofitInstance

object Repositories {
    private lateinit var applicationContext: Context

    val toDoRepository: ToDoRepository by lazy {
        ToDoRepository()
    }

    val toDoDbRepository: LocalDbRepository by lazy{
        LocalDbRepository(ToDoDatabase.getDatabase(applicationContext).toDoItemDao())
    }

    val toDoNetworkRepository: ToDoNetworkRepository by lazy {
        ToDoNetworkRepository(RetrofitInstance.api)
    }

    fun init(context: Context){
        applicationContext = context
    }
}