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

    suspend fun addNewItem(item: ToDoItem) : RequestStatus {
        try {
            val elementRequest = ElementRequest(item)
            if(lastKnownRevision == illegalRevision) refreshItems()
            val response = api.addElement(lastKnownRevision, elementRequest)
            if (response.isSuccessful) {
                refreshItems()
            }
            return if (response.isSuccessful) RequestStatus.Success else RequestStatus.SynchronizeError
        }
        catch (e: Exception){
            Log.e("exceptions", e.message.toString())
            return RequestStatus.NetworkError
        }
    }

    suspend fun updateItems(items: List<ToDoItem>): RequestStatus{
        try {
            val listRequest = ListRequest(items)
            if(lastKnownRevision == illegalRevision) refreshItems()
            val response = api.updateList(lastKnownRevision!!, listRequest)
            if(response.isSuccessful){
                refreshItems()
            }
            return if (response.isSuccessful) RequestStatus.Success else RequestStatus.SynchronizeError
        }
        catch (e: Exception){
            Log.e("exceptions", e.message.toString())
            return RequestStatus.NetworkError
        }
    }

    suspend fun getItems(): MutableStateFlow<List<ToDoItem>> {
        refreshItems()
        return _toDoItemsFlow
    }

    suspend fun getById(id: String): ToDoItem? {
        try {
            val response = api.getElement(id)
            lastKnownRevision = response.body()?.revision ?: illegalRevision
            return if (response.isSuccessful) {
                response.body()?.element
            } else {
                null
            }
        }
        catch (e: Exception){
            Log.e("exceptions", e.message.toString())
            return null
        }
    }

    suspend fun changeItem(toDoItem: ToDoItem): RequestStatus {
        try {
            if (lastKnownRevision == illegalRevision) refreshItems()
            val response = api.updateElement(lastKnownRevision, ElementRequest(toDoItem), toDoItem.id)
            if(response.isSuccessful){
                refreshItems()
            }
            return if (response.isSuccessful) RequestStatus.Success else RequestStatus.SynchronizeError
        }
        catch (e: Exception){
            Log.e("exceptions", e.message.toString())
            return RequestStatus.NetworkError
        }
    }

    suspend fun deleteItem(id: String): RequestStatus {
        try {
            if (lastKnownRevision == illegalRevision) refreshItems()
            val response = api.deleteElement(lastKnownRevision, id)
            if (response.isSuccessful) {
                refreshItems()
            }
            return if (response.isSuccessful) RequestStatus.Success else RequestStatus.SynchronizeError
        }
        catch (e: Exception){
            Log.e("exceptions", e.message.toString())
            return RequestStatus.NetworkError
        }
    }

    private suspend fun refreshItems() {
        try {
            val response = api.getList()
            if (response.isSuccessful) {
                lastKnownRevision = response.body()?.revision ?: illegalRevision
                val items = response.body()?.list ?: emptyList()
                _toDoItemsFlow.value = items
            }
        }
        catch (e: Exception){
            Log.e("exceptions", e.message.toString())
        }
    }

    enum class RequestStatus{
        NetworkError,
        SynchronizeError,
        Success
    }

    companion object{
        const val illegalRevision = -1
    }
}