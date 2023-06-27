package com.example.todolist.Model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.MutableStateFlow

@Dao
interface ToDoItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToDoItem(toDoItem: ToDoItem)

    @Update
    suspend fun updateToDoItem(toDoItem: ToDoItem)

    @Delete
    suspend fun deleteToDoItem(toDoItem: ToDoItem)

    @Query("SELECT * FROM to_do_items_table")
    fun readAllToDoItems(): List<ToDoItem>
}