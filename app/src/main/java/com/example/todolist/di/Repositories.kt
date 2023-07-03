package com.example.todolist.di

import android.content.Context
import com.example.todolist.data.db.ToDoDatabase
import com.example.todolist.data.api.RetrofitInstance
import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.data.source.LocalDbSource
import com.example.todolist.data.source.NetworkSource

object Repositories {
    private lateinit var applicationContext: Context

    val toDoRepository: ToDoRepository by lazy {
        ToDoRepository()
    }

    val toDoDbRepository: LocalDbSource by lazy{
        LocalDbSource(ToDoDatabase.getDatabase(applicationContext).toDoItemDao())
    }

    val toDoNetworkRepository: NetworkSource by lazy {
        NetworkSource(RetrofitInstance.api)
    }

    fun init(context: Context){
        applicationContext = context
    }
}