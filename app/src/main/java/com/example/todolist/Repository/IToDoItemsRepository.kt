package com.example.todolist.Repository

import com.example.todolist.Model.ToDoItem
import kotlinx.coroutines.flow.MutableStateFlow

interface IToDoItemsRepository {
    suspend fun addNewItem(item: ToDoItem)
    //fun addToBegin(item: ToDoItem)
    suspend fun getItems(): MutableStateFlow<List<ToDoItem>>
    //fun addListener(listener: ToDoListListener)
    //fun removeListener(listener: ToDoListListener)
    suspend fun getById(id: String) : ToDoItem?
    suspend fun changeItem(task: ToDoItem)
    suspend fun deleteItem(id: String)
    fun moveItem(task: ToDoItem, moveBy: Int)
    //fun getCompletedTasksCount(): Int
    //fun getCompletedTasks(): ArrayList<ToDoItem>
}