package com.example.todolist.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface ToDoItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToDoItem(toDoItem: ToDoItem)

    @Update
    suspend fun updateToDoItem(toDoItem: ToDoItem)

    @Query("SELECT * FROM to_do_items_table WHERE id = :id")
    suspend fun getToDoItemById(id: String): ToDoItem?

    @Query("DELETE FROM to_do_items_table WHERE id = :id")
    suspend fun deleteToDoItem(id: String)

    @Query("SELECT * FROM to_do_items_table")
    fun readAllToDoItems(): Flow<List<ToDoItem>>
}