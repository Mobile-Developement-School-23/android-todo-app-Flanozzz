package com.example.todolist.data.source

import android.util.Log
import com.example.todolist.data.model.ElementRequest
import com.example.todolist.data.model.ListRequest
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.data.api.ToDoApi
import com.example.todolist.utils.Constants.Companion.BAD_REQUEST_CODE
import kotlinx.coroutines.delay
import retrofit2.Response


class NetworkSource(private val api: ToDoApi) {

    private var toDoItems: List<ToDoItem> = emptyList()
    private var lastKnownRevision = illegalRevision

    suspend fun addNewItem(item: ToDoItem) : ResponseStatus {
        try {
            val elementRequest = ElementRequest(item)
            if(lastKnownRevision == illegalRevision) refreshItems()
            val response = makeRequest { api.addElement(lastKnownRevision, elementRequest) }
            if (response.isSuccessful) refreshItems()
            return if(response.isSuccessful) ResponseStatus.Successful else ResponseStatus.Unsuccessful
        }
        catch (e: Exception){
            Log.e("exceptions", e.message.toString())
            return ResponseStatus.NetworkError
        }
    }

    suspend fun updateItems(items: List<ToDoItem>): ResponseStatus {
        try {
            val listRequest = ListRequest(items)
            if (lastKnownRevision == illegalRevision) refreshItems()
            val response = makeRequest { api.updateList(lastKnownRevision, listRequest) }
            if(response.isSuccessful) refreshItems()
            if(response.isSuccessful){
                refreshItems()
            }
            return if(response.isSuccessful) ResponseStatus.Successful else ResponseStatus.Unsuccessful
        }
        catch (e: Exception){
            Log.e("exceptions", e.message.toString())
            return ResponseStatus.NetworkError
        }
    }

    suspend fun getItems(): List<ToDoItem> {
        refreshItems()
        return toDoItems
    }

    suspend fun getById(id: String): ToDoItem? {
        try {
            val response = makeRequest { api.getElement(id) }
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

    suspend fun changeItem(toDoItem: ToDoItem): ResponseStatus {
        try {
            if (lastKnownRevision == illegalRevision) refreshItems()
            val response = makeRequest {
                api.updateElement(lastKnownRevision, ElementRequest(toDoItem), toDoItem.id)
            }
            if(response.isSuccessful) refreshItems()
            return if(response.isSuccessful) ResponseStatus.Successful else ResponseStatus.Unsuccessful
        }
        catch (e: Exception){
            Log.e("exceptions", e.message.toString())
            return ResponseStatus.NetworkError
        }
    }

    suspend fun deleteItem(id: String): ResponseStatus {
        try {
            val response = makeRequest { api.deleteElement(lastKnownRevision, id) }
            if(response.isSuccessful) refreshItems()
            return if(response.isSuccessful) ResponseStatus.Successful else ResponseStatus.Unsuccessful
        }
        catch (e: Exception){
            Log.e("exceptions", e.message.toString())
            return ResponseStatus.NetworkError
        }
    }

    private suspend fun refreshItems(){
        try {
            val response = makeRequest { api.getList() }
            if (response.isSuccessful) {
                lastKnownRevision = response.body()?.revision ?: illegalRevision
                val items = response.body()?.list ?: emptyList()
                toDoItems = items
            }
        }
        catch (e: Exception){
            Log.e("exceptions", e.message.toString())
        }
    }

    private suspend fun <T> makeRequest(apiRequest: suspend () -> Response<T>): Response<T>{
        var response: Response<T>
        var responseCount = 0

        do {
            response = apiRequest()
            if (response.code() == BAD_REQUEST_CODE) break
            if(response.isSuccessful){
                break
            }
            delay(1000)
            responseCount++
        } while (responseCount < 3)

        return response
    }

    enum class ResponseStatus(){
        NetworkError,
        Successful,
        Unsuccessful,
    }

    companion object{
        const val illegalRevision = -1
    }
}