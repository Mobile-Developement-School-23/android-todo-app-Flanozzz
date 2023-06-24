package com.example.todolist.RetorfitTest

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://beta.mrdekk.ru/todobackend/"

    private val retrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "Bearer unsalesmanlike")
                .build()
            chain.proceed(request)
        }.build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ListApi by lazy {
        retrofit.create(ListApi::class.java)
    }
}