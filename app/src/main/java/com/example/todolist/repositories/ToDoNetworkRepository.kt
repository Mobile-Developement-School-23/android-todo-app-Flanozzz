package com.example.todolist.repositories

import android.util.Log
import com.example.todolist.api.ElementRequest
import com.example.todolist.api.ListRequest
import com.example.todolist.models.ToDoItem
import com.example.todolist.api.ToDoApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response


class ToDoNetworkRepository(private val api: ToDoApi) {

    private val _toDoItemsFlow: MutableStateFlow<List<ToDoItem>> = MutableStateFlow(emptyList())
    private var lastKnownRevision = illegalRevision

    suspend fun addNewItem(item: ToDoItem) : RequestStatus {
        try {
            val elementRequest = ElementRequest(item)
            if(lastKnownRevision == illegalRevision) refreshItems()
            val response = makeRequest { api.addElement(lastKnownRevision, elementRequest) }
            if (response.isSuccessful) refreshItems()
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
            if (lastKnownRevision == illegalRevision) refreshItems()
            val response = makeRequest { api.updateList(lastKnownRevision, listRequest) }
            if(response.isSuccessful) refreshItems()
//            if(lastKnownRevision == illegalRevision) refreshItems()
//            val response = api.updateList(lastKnownRevision!!, listRequest)
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

    suspend fun changeItem(toDoItem: ToDoItem): RequestStatus {
        try {
            if (lastKnownRevision == illegalRevision) refreshItems()
            val response = makeRequest {
                api.updateElement(lastKnownRevision, ElementRequest(toDoItem), toDoItem.id)
            }
            if(response.isSuccessful) refreshItems()
            return if (response.isSuccessful) RequestStatus.Success else RequestStatus.SynchronizeError
        }
        catch (e: Exception){
            Log.e("exceptions", e.message.toString())
            return RequestStatus.NetworkError
        }
    }

    suspend fun deleteItem(id: String): RequestStatus {
        try {
            val response = makeRequest { api.deleteElement(lastKnownRevision, id) }
//            if (lastKnownRevision == illegalRevision) refreshItems()
//            var response: Response<ElementRequest>
//            var responseCount = 0
//            do {
//                response = api.deleteElement(lastKnownRevision, id)
//                if(response.isSuccessful){
//                    refreshItems()
//                    break
//                }
//                delay(1000)
//                responseCount++
//            } while (responseCount < 3)
//            return if (response.isSuccessful) RequestStatus.Success else RequestStatus.SynchronizeError

//            if (lastKnownRevision == illegalRevision) refreshItems()
//            val response = api.deleteElement(lastKnownRevision, id)
//            if (response.isSuccessful) {
//                refreshItems()
//            }
            if(response.isSuccessful) refreshItems()
            return if (response.isSuccessful) RequestStatus.Success else RequestStatus.SynchronizeError
        }
        catch (e: Exception){
            Log.e("exceptions", e.message.toString())
            return RequestStatus.NetworkError
        }
    }

    private suspend fun refreshItems() {
        try {
            val response = makeRequest { api.getList() }
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

    private suspend fun <T> makeRequest(apiRequest: suspend () -> Response<T>): Response<T>{
        var response: Response<T>
        var responseCount = 0
        //response = apiRequest()

        do {
            response = apiRequest()
            if (response.code() == RequestStatus.BadRequest.value) break
            if(response.isSuccessful){
                return response
            }
            delay(1000)
            responseCount++
        } while (responseCount < 3)

        return response
    }



    enum class RequestStatus(val value: Int){
        NetworkError(0),
        SynchronizeError(1),
        Success(200),
        BadRequest(400)
    }

    companion object{
        const val illegalRevision = -1
    }
}