package com.example.todolist.repositories

import android.content.Context
import com.example.todolist.models.ToDoDatabase
import com.example.todolist.api.RetrofitInstance

object Repositories {
    private lateinit var applicationContext: Context

    val toDoRepository: IToDoItemsRepository by lazy {
        ToDoRepository()
    }

    val toDoDbRepository: IToDoItemsRepository by lazy{
        LocalDbRepository(ToDoDatabase.getDatabase(applicationContext).toDoItemDao())
    }

    val toDoNetworkRepository: IToDoItemsRepository by lazy {
        ToDoNetworkRepository(RetrofitInstance.api)
    }

    fun init(context: Context){
        applicationContext = context
    }
}