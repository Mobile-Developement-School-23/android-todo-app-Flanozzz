package com.example.todolist.Repository

import com.example.todolist.Model.ToDoItem

interface IToDoItemsRepository {
    fun addNewItem(item: ToDoItem)
    fun addToBegin(item: ToDoItem)
    fun getItems(): ArrayList<ToDoItem>
    fun addListener(listener: ToDoListListener)
    fun removeListener(listener: ToDoListListener)
    fun getById(id: String) : ToDoItem
    fun changeItem(task: ToDoItem, notifyListeners: Boolean = true)
    fun deleteItem(id: String)
    fun moveItem(task: ToDoItem, moveBy: Int)
    fun getCompletedTasksCount(): Int
    fun getCompletedTasks(): ArrayList<ToDoItem>
}