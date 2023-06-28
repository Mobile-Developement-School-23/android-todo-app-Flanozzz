package com.example.todolist.Repository

import com.example.todolist.Model.ToDoItem
import kotlinx.coroutines.flow.MutableStateFlow

class ToDoRepository(): IToDoItemsRepository {


    override suspend fun addNewItem(item: ToDoItem) {
        TODO("Not yet implemented")
    }

    override suspend fun getItems(): MutableStateFlow<List<ToDoItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: String): ToDoItem? {
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
}