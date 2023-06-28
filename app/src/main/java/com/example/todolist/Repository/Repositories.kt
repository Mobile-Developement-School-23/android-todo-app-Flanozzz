package com.example.todolist.Repository

import android.content.Context
import com.example.todolist.Model.ToDoDatabase
import com.example.todolist.api.RetrofitInstance

object Repositories {

    private lateinit var applicationContext: Context

    val localRepository: IToDoItemsRepository by lazy {
        LocalToDoItemsRepository()
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