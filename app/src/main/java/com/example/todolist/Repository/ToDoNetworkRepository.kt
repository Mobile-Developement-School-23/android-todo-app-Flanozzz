package com.example.todolist.Repository

import androidx.lifecycle.LiveData
import com.example.todolist.Model.ToDoItem
import com.example.todolist.Model.ToDoItemDao
import kotlinx.coroutines.flow.MutableStateFlow

class ToDoNetworkRepository : IToDoItemsRepository {

    private var toDoItems = ArrayList<ToDoItem>()

    private suspend fun loadItems(): List<ToDoItem>{

    }

    override suspend fun addNewItem(item: ToDoItem) {
        TODO("Not yet implemented")
    }

    override fun getItems(): MutableStateFlow<List<ToDoItem>> {
        return MutableStateFlow(toDoItems)
    }

    override fun getById(id: String): ToDoItem? {
        TODO("Not yet implemented")
    }

    override suspend fun changeItem(task: ToDoItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteItem(id: String) {
        TODO("Not yet implemented")
    }

    override fun moveItem(task: ToDoItem, moveBy: Int) {
        TODO("Not yet implemented")
    }

    override fun getCompletedTasksCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getCompletedTasks(): ArrayList<ToDoItem> {
        TODO("Not yet implemented")
    }

}