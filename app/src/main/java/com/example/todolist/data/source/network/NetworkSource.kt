package com.example.todolist.data.source.network

import android.util.Log
import com.example.todolist.data.model.ElementRequest
import com.example.todolist.data.model.ListRequest
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.data.api.ToDoApi
import com.example.todolist.data.model.ElementResponse
import com.example.todolist.data.model.ListResponse
import com.example.todolist.di.scopes.AppScope
import com.example.todolist.utils.Constants.Companion.BAD_REQUEST_CODE
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Inject


@AppScope
class NetworkSource @Inject constructor(private val api: ToDoApi) {

    private var toDoItems: List<ToDoItem> = emptyList()
    private var lastKnownRevision = illegalRevision

    suspend fun addNewItem(item: ToDoItem) {
        var response: Response<ElementResponse>? = null
        try {
            val elementRequest = ElementRequest(item)
            if(lastKnownRevision == illegalRevision) refreshItems()
            response = makeRequest { api.addElement(lastKnownRevision, elementRequest) }
            if (response.isSuccessful) refreshItems()
        }
        catch (_: Exception){}
        if(response != null && !response.isSuccessful){
            throw UnsuccessfulResponseException("Unsuccessful response")
        }
    }

    suspend fun updateItems(items: List<ToDoItem>) {
        var response: Response<ListResponse>? = null
        try {
            val listRequest = ListRequest(items)
            if (lastKnownRevision == illegalRevision) refreshItems()
            response = makeRequest { api.updateList(lastKnownRevision, listRequest) }
            if(response.isSuccessful) refreshItems()
        }
        catch (_: Exception){}
        if(response != null && !response.isSuccessful){
            throw UnsuccessfulResponseException("Unsuccessful response")
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

    suspend fun changeItem(toDoItem: ToDoItem) {
        var response: Response<ElementRequest>? = null
        try {
            if (lastKnownRevision == illegalRevision) refreshItems()
            response = makeRequest {
                api.updateElement(lastKnownRevision, ElementRequest(toDoItem), toDoItem.id)
            }
            if(response.isSuccessful) refreshItems()
        }
        catch (_: Exception){}
        if(response != null && !response.isSuccessful){
            throw UnsuccessfulResponseException("Unsuccessful response")
        }
    }

    suspend fun deleteItem(id: String) {
        var response: Response<ElementRequest>? = null
        try {
            response = makeRequest { api.deleteElement(lastKnownRevision, id) }
            if(response.isSuccessful) refreshItems()
        }
        catch (_: Exception){}
        if(response != null && !response.isSuccessful){
            throw UnsuccessfulResponseException("Unsuccessful response")
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
        catch (_: Exception){}
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

//    enum class ResponseStatus(){
//        NetworkError,
//        Successful,
//        Unsuccessful,
//    }

    companion object{
        const val illegalRevision = -1
    }
}