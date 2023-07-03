package com.example.todolist.data.api

import com.example.todolist.data.model.ToDoItem
import com.example.todolist.utils.Constants.Companion.BASEURL
import com.example.todolist.utils.Constants.Companion.TOKEN
import com.example.todolist.utils.ImportanceConverter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val httpClient by lazy{
        OkHttpClient.Builder().addInterceptor{chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "Bearer $TOKEN")
                .build()
            chain.proceed(request)
        }.build()
    }

    private val gson by lazy{
        GsonBuilder()
            .registerTypeAdapter(ToDoItem.Importance::class.java, ImportanceConverter())
            .create()
    }

    private val retrofit by lazy{

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