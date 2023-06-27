package com.example.todolist

import android.app.Application
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.todolist.Model.ToDoDatabase
import com.example.todolist.Model.ToDoItem
import com.example.todolist.Repository.IToDoItemsRepository
import com.example.todolist.Repository.LocalDbRepository
import com.example.todolist.Repository.LocalToDoItemsRepository
import com.example.todolist.Repository.Repositories
import com.example.todolist.Utils.getCurrentUnixTime
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class App : Application() {
    //val toDoItemsRepository = LocalToDoItemsRepository()
    //private lateinit var toDoDbItemsRepository: LocalDbRepository

    override fun onCreate() {
        super.onCreate()
        Repositories.init(context = this)
    }
}