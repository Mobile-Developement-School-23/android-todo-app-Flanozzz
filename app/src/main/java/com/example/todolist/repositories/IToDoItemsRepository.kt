package com.example.todolist.repositories

import com.example.todolist.models.ToDoItem
import kotlinx.coroutines.flow.Flow

interface IToDoItemsRepository {
    suspend fun addNewItem(item: ToDoItem)
    //fun addToBegin(item: ToDoItem)
    suspend fun getItems(): Flow<List<ToDoItem>>
    //fun addListener(listener: ToDoListListener)
    //fun removeListener(listener: ToDoListListener)
    suspend fun getById(id: String) : ToDoItem?
    suspend fun changeItem(toDoItem: ToDoItem)
    suspend fun deleteItem(id: String)

}