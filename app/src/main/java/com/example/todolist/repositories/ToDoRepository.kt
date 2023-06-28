package com.example.todolist.repositories

import android.util.Log
import com.example.todolist.models.ToDoItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
class ToDoRepository: IToDoItemsRepository {

    private val toDoDbRepository by lazy {
        Repositories.toDoDbRepository
    }

    private val toDoNetworkRepository by lazy {
        Repositories.toDoNetworkRepository
    }

    override suspend fun addNewItem(item: ToDoItem) {
        toDoDbRepository.addNewItem(item)
    }

    override suspend fun getItems(): Flow<List<ToDoItem>> = toDoDbRepository.getItems()

    override suspend fun getById(id: String): ToDoItem? {
        return toDoDbRepository.getById(id)
    }

    override suspend fun changeItem(toDoItem: ToDoItem) {
        toDoDbRepository.changeItem(toDoItem)

    }

    override suspend fun deleteItem(id: String) {
        toDoDbRepository.deleteItem(id)
    }
}