package com.example.todolist.repositories

import com.example.todolist.models.ToDoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ToDoRepository {

    private val toDoDbRepository = Repositories.toDoDbRepository
    private val toDoNetworkRepository = Repositories.toDoNetworkRepository

    suspend fun syncData(): ToDoNetworkRepository.ResponseStatus{
        val remotedData = toDoNetworkRepository.getItems()
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
        return toDoNetworkRepository.updateItems(data)
    }

    suspend fun addNewItem(item: ToDoItem): ToDoNetworkRepository.ResponseStatus {
        toDoDbRepository.addNewItem(item)
        return toDoNetworkRepository.addNewItem(item)
    }

    suspend fun getItems(): Flow<List<ToDoItem>> = toDoDbRepository.getItems()

    suspend fun getById(id: String): ToDoItem? {
        var result = toDoDbRepository.getById(id)
        if (result == null) result = toDoNetworkRepository.getById(id)
        return result
    }

    suspend fun changeItem(toDoItem: ToDoItem): ToDoNetworkRepository.ResponseStatus {
        toDoDbRepository.changeItem(toDoItem)
        return toDoNetworkRepository.changeItem(toDoItem)
    }

    suspend fun deleteItem(id: String): ToDoNetworkRepository.ResponseStatus {
        toDoDbRepository.deleteItem(id)
        return toDoNetworkRepository.deleteItem(id)
    }
}