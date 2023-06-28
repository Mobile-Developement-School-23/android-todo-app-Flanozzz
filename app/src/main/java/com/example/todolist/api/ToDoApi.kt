package com.example.todolist.api

import com.example.todolist.models.ElementRequest
import com.example.todolist.models.ElementResponse
import com.example.todolist.models.ListRequest
import com.example.todolist.models.ListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ToDoApi {
    @GET("list")
    suspend fun getList(): Response<ListResponse>

    @GET("list/{id}")
    suspend fun getElement(@Path("id") id: String): Response<ElementRequest>

    @PATCH("list")
    suspend fun updateList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: ListRequest
    ): Response<ListResponse>

    @POST("list")
    suspend fun addElement(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: ElementRequest
    ): Response<ElementResponse>

    @PUT("list/{id}")
    suspend fun updateElement(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: ElementRequest,
        @Path("id") id: String
    ): Response<ElementRequest>

    @DELETE("list/{id}")
    suspend fun deleteElement(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String
    ): Response<ElementRequest>
}