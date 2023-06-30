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

        localData.forEach { map[it.id] = it }
        remotedData.forEach {
            if(map.containsKey(it.id)){
                val localDateOfChange = map[it.id]!!.dateOfChange
                val remotedDateOfChange = it.dateOfChange
                if (localDateOfChange < remotedDateOfChange) map[it.id] = it
            } else {
                map[it.id] = it
            }
        }

        data.addAll(map.values)
        toDoDbRepository.addNewItems(data)
        toDoNetworkRepository.updateItems(data)
    }

    suspend fun addNewItem(item: ToDoItem): ToDoNetworkRepository.RequestStatus {
        toDoDbRepository.addNewItem(item)
        return toDoNetworkRepository.addNewItem(item)
    }

    suspend fun getItems(): Flow<List<ToDoItem>> = toDoDbRepository.getItems()

    suspend fun getById(id: String): ToDoItem? {
        var result = toDoDbRepository.getById(id)
        if (result == null) result = toDoNetworkRepository.getById(id)
        return result
    }

    suspend fun changeItem(toDoItem: ToDoItem): ToDoNetworkRepository.RequestStatus {
        toDoDbRepository.changeItem(toDoItem)
        return toDoNetworkRepository.changeItem(toDoItem)
    }

    suspend fun deleteItem(id: String): ToDoNetworkRepository.RequestStatus {
        toDoDbRepository.deleteItem(id)
        return toDoNetworkRepository.deleteItem(id)
    }
}