package com.example.todolist.repositories

import android.util.Log
import com.example.todolist.models.ToDoItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import java.net.NetPermission

class ToDoRepository {

    private val toDoDbRepository = Repositories.toDoDbRepository
    private val toDoNetworkRepository = Repositories.toDoNetworkRepository

    suspend fun syncData(){
        val remotedData = toDoNetworkRepository.getItems().value
        val localData: List<ToDoItem> = toDoDbRepository.getItems().first()
        val data = mutableListOf<ToDoItem>()
        val map = mutableMapOf<String, ToDoItem>()
        for (item in localData) {
            map[item.id] = item
        }
        for (item in remotedData) {
            map[item.id] = item
        }
        data.addAll(map.values)
        toDoDbRepository.addNewItems(data)
        toDoNetworkRepository.updateItems(data)
    }

    suspend fun addNewItem(item: ToDoItem): Boolean {
        toDoDbRepository.addNewItem(item)
        return toDoNetworkRepository.addNewItem(item)
    }

    suspend fun getItems(): Flow<List<ToDoItem>> = toDoDbRepository.getItems()

    suspend fun getById(id: String): ToDoItem? {
        var result = toDoDbRepository.getById(id)
        if (result == null) result = toDoNetworkRepository.getById(id)
        return result
    }

    suspend fun changeItem(toDoItem: ToDoItem): Boolean {
        toDoDbRepository.changeItem(toDoItem)
        return toDoNetworkRepository.changeItem(toDoItem)
    }

    suspend fun deleteItem(id: String): Boolean {
        toDoDbRepository.deleteItem(id)
        return toDoNetworkRepository.deleteItem(id)
    }
}