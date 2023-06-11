package com.example.todolist

import android.app.Application
import com.example.todolist.Repository.LocalToDoItemsRepository

class App : Application() {
    val toDoItemsRepository = LocalToDoItemsRepository()

}