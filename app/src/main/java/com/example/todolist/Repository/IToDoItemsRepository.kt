package com.example.todolist.Repository

import com.example.todolist.Model.ToDoItem

interface IToDoItemsRepository {
    fun addNewItem(item: ToDoItem)
    fun getItems(): ArrayList<ToDoItem>
    fun addListener(listener: ToDoListListener)
    fun removeListener(listener: ToDoListListener)
    fun getById(id: String) : ToDoItem
    fun changeItem(task: ToDoItem)
    fun deleteItem(id: String)
    fun createNewItem() : ToDoItem
}