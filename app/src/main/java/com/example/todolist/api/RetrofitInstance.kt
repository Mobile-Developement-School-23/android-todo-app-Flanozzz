package com.example.todolist.api

import com.example.todolist.models.ToDoItem
import com.example.todolist.utils.Constants.Companion.BASEURL
import com.example.todolist.utils.Constants.Companion.TOKEN
import com.example.todolist.utils.ImportanceConverter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy{
        val httpClient = OkHttpClient.Builder().addInterceptor{chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "Bearer $TOKEN")
                .build()
            chain.proceed(request)
        }.build()

        val gson = GsonBuilder()
            .registerTypeAdapter(ToDoItem.Importance::class.java, ImportanceConverter())
            .create()

        Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val api: ToDoApi by lazy {
        retrofit.create(ToDoApi::class.java)
    }
}