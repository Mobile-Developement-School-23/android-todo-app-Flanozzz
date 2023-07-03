package com.example.todolist.data.repository

import com.example.todolist.data.source.NetworkSource
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.di.Repositories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ToDoRepository {

    private val dbSource = Repositories.toDoDbRepository
    private val networkSource = Repositories.toDoNetworkRepository

    suspend fun syncData(): NetworkSource.ResponseStatus{
        val remotedData = networkSource.getItems()
        val localData: List<ToDoItem> = dbSource.getItems().first()
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
        dbSource.addNewItems(data)
        return networkSource.updateItems(data)
    }

    suspend fun addNewItem(item: ToDoItem): NetworkSource.ResponseStatus {
        dbSource.addNewItem(item)
        return networkSource.addNewItem(item)
    }

    suspend fun getItems(): Flow<List<ToDoItem>> = dbSource.getItems()

    suspend fun getById(id: String): ToDoItem? {
        var result = dbSource.getById(id)
        if (result == null) result = networkSource.getById(id)
        return result
    }

    suspend fun changeItem(toDoItem: ToDoItem): NetworkSource.ResponseStatus {
        dbSource.changeItem(toDoItem)
        return networkSource.changeItem(toDoItem)
    }

    suspend fun deleteItem(id: String): NetworkSource.ResponseStatus {
        dbSource.deleteItem(id)
        return networkSource.deleteItem(id)
    }
}