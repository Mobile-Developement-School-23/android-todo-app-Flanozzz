package com.example.todolist.data.repository

import com.example.todolist.data.model.ToDoItem
import kotlinx.coroutines.flow.Flow

interface IRepository {

    suspend fun syncData()
    suspend fun addNewItem(item: ToDoItem)
    suspend fun getItems(): Flow<List<ToDoItem>>
    suspend fun getById(id: String) : ToDoItem?
    suspend fun changeItem(toDoItem: ToDoItem)
    suspend fun deleteItem(id: String)

}