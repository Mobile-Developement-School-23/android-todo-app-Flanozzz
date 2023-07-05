package com.example.todolist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.data.model.ToDoItem

@Database(entities = [ToDoItem::class], version = 2, exportSchema = false)
abstract class ToDoDatabase: RoomDatabase() {

    abstract fun toDoItemDao(): ToDoItemDao
}