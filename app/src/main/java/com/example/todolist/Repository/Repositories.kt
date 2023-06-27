package com.example.todolist.Repository

import android.content.Context
import com.example.todolist.Model.ToDoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repositories {

    private lateinit var applicationContext: Context

    val localRepository: IToDoItemsRepository by lazy {
        LocalToDoItemsRepository()
    }

    val toDoDbRepository: IToDoItemsRepository by lazy{
        LocalDbRepository(ToDoDatabase.getDatabase(applicationContext).toDoItemDao())
    }

    fun init(context: Context){
        applicationContext = context
    }
}