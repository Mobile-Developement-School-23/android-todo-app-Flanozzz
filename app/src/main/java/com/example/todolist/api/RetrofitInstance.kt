package com.example.todolist.api

import com.example.todolist.Utils.Constants.Companion.BASEURL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {
    private val retrofit by lazy{
        val httpClient = OkHttpClient.Builder().addInterceptor{chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "Bearer unsalesmanlike")
                .build()
            chain.proceed(request)
        }.build()

        Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ToDoApi by lazy {
        retrofit.create(ToDoApi::class.java)
    }
}