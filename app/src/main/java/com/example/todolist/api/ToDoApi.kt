package com.example.todolist.api

import com.example.todolist.Model.ListElement
import com.example.todolist.RetorfitTest.ListRequest
import com.example.todolist.RetorfitTest.ListResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ToDoApi {
    @GET("list")
    suspend fun getList(): Response<ListResponse>

    @PATCH("list")
    suspend fun updateList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: ListRequest
    ): Response<ListResponse>

    @POST("list")
    suspend fun addElement(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: ListElement
    ): Response<ListElement>
}