package com.example.todolist.Repository

import com.example.todolist.Model.ElementRequest
import com.example.todolist.Model.ToDoItem
import com.example.todolist.api.ToDoApi
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.RuntimeException


class ToDoNetworkRepository(private val api: ToDoApi) : IToDoItemsRepository {

    private val _toDoItemsFlow: MutableStateFlow<List<ToDoItem>> = MutableStateFlow(emptyList())

    private var lastKnownRevision: Int? = null

    override suspend fun addNewItem(item: ToDoItem) {
        val elementRequest = ElementRequest(item)
        if(lastKnownRevision != null){
            val response = api.addElement(lastKnownRevision!!, elementRequest)
            if (response.isSuccessful) {
                refreshItems()
            }
            else {
                throw RuntimeException("unsuccessful request")
            }
        }
    }

    override suspend fun getItems(): MutableStateFlow<List<ToDoItem>> {
        refreshItems()
        return _toDoItemsFlow
    }

    override suspend fun getById(id: String): ToDoItem? {
        return _toDoItemsFlow.value.find { it.id == id }
    }

    override suspend fun changeItem(task: ToDoItem) {
        if (lastKnownRevision != null){
            val response = api.updateElement(lastKnownRevision!!, ElementRequest(task), task.id)
            if (response.isSuccessful) {
                refreshItems()
            }
            else {
                throw RuntimeException("unsuccessful request")
            }
        }
    }

    override suspend fun deleteItem(id: String) {
        if(lastKnownRevision != null){
            val response = api.deleteElement(lastKnownRevision!!, id)
            if (response.isSuccessful) {
                refreshItems()
            }
            else {
                throw RuntimeException("unsuccessful request")
            }
        }
    }

    override fun moveItem(task: ToDoItem, moveBy: Int) {
        TODO("Not yet implemented")
    }

    private suspend fun refreshItems() {
        val response = api.getList()
        if (response.isSuccessful) {
            lastKnownRevision = response.body()?.revision
            val items = response.body()?.list ?: emptyList()
            _toDoItemsFlow.value = items
        }
        else {
            throw RuntimeException("unsuccessful request")
        }
    }
}