package com.example.todolist.repositories

import android.util.Log
import com.example.todolist.api.ElementRequest
import com.example.todolist.api.ListRequest
import com.example.todolist.models.ToDoItem
import com.example.todolist.api.ToDoApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import java.lang.RuntimeException


class ToDoNetworkRepository(private val api: ToDoApi) {

    private val _toDoItemsFlow: MutableStateFlow<List<ToDoItem>> = MutableStateFlow(emptyList())
    private var lastKnownRevision = illegalRevision

    suspend fun addNewItem(item: ToDoItem) : Boolean {
        val elementRequest = ElementRequest(item)
        if(lastKnownRevision == illegalRevision) refreshItems()
        var response = api.addElement(lastKnownRevision, elementRequest)
        if (response.isSuccessful) {
            refreshItems()
        }
        return response.isSuccessful
    }

    suspend fun updateItems(items: List<ToDoItem>): Boolean{
        val listRequest = ListRequest(items)
        if(lastKnownRevision == illegalRevision) refreshItems()
        var response = api.updateList(lastKnownRevision!!, listRequest)
        if(response.isSuccessful){
            refreshItems()
        }
        return response.isSuccessful
    }

    suspend fun getItems(): MutableStateFlow<List<ToDoItem>> {
        refreshItems()
        return _toDoItemsFlow
    }

    suspend fun getById(id: String): ToDoItem? {
        val response = api.getElement(id)
        lastKnownRevision = response.body()?.revision ?: illegalRevision
        return if (response.isSuccessful) {
            response.body()?.element
        } else {
            null
        }
    }

    suspend fun changeItem(toDoItem: ToDoItem): Boolean {
        if (lastKnownRevision == illegalRevision) refreshItems()
        var response = api.updateElement(lastKnownRevision, ElementRequest(toDoItem), toDoItem.id)
        if(response.isSuccessful){
            refreshItems()
        }
        return response.isSuccessful
    }

    suspend fun deleteItem(id: String): Boolean {
        if (lastKnownRevision == illegalRevision) refreshItems()
        var response = api.deleteElement(lastKnownRevision, id)
        if (response.isSuccessful) {
            refreshItems()
        }
        return response.isSuccessful
    }

    private suspend fun refreshItems() {
        val response = api.getList()
        if (response.isSuccessful) {
            lastKnownRevision = response.body()?.revision ?: illegalRevision
            val items = response.body()?.list ?: emptyList()
            _toDoItemsFlow.value = items
        }
    }

    companion object{
        const val illegalRevision = -1
    }
}