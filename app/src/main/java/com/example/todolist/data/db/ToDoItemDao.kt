package com.example.todolist.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todolist.data.model.ToDoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToDoItem(toDoItem: ToDoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToDoItems(toDoItems: List<ToDoItem>)

    @Update
    suspend fun updateToDoItem(toDoItem: ToDoItem)

    @Query("SELECT * FROM to_do_items_table WHERE id = :id")
    suspend fun getToDoItemById(id: String): ToDoItem?

    @Query("DELETE FROM to_do_items_table WHERE id = :id")
    suspend fun deleteToDoItem(id: String)

    @Query("SELECT * FROM to_do_items_table")
    fun readAllToDoItems(): Flow<List<ToDoItem>>
}