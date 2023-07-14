package com.example.todolist.data.repository

import com.example.todolist.data.source.network.NetworkSource
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.data.source.LocalDbSource
import com.example.todolist.di.scopes.AppScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AppScope
class ToDoRepository @Inject constructor(
    private val dbSource: LocalDbSource,
    private val networkSource: NetworkSource
) : IRepository {

    override suspend fun syncData(){
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
        networkSource.updateItems(data)
    }

    override suspend fun addNewItem(item: ToDoItem) {
        dbSource.addNewItem(item)
        networkSource.addNewItem(item)
    }

    override suspend fun getItems(): Flow<List<ToDoItem>> = dbSource.getItems()

    override suspend fun getById(id: String): ToDoItem? {
        var result = dbSource.getById(id)
        if (result == null) result = networkSource.getById(id)
        return result
    }

    override suspend fun changeItem(toDoItem: ToDoItem) {
        dbSource.changeItem(toDoItem)
        networkSource.changeItem(toDoItem)
    }

    override suspend fun deleteItem(id: String){
        dbSource.deleteItem(id)
        networkSource.deleteItem(id)
    }
}