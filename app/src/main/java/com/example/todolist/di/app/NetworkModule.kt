package com.example.todolist.di.app

import com.example.todolist.data.api.ToDoApi
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.utils.Constants.BASEURL
import com.example.todolist.utils.Constants.TOKEN
import com.example.todolist.data.api.ImportanceConverter
import com.example.todolist.di.scopes.AppScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object NetworkModule {

    @Provides
    @AppScope
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", "Bearer $TOKEN")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @AppScope
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(ToDoItem.Importance::class.java, ImportanceConverter())
            .create()
    }

    @Provides
    @AppScope
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @AppScope
    fun provideToDoApi(retrofit: Retrofit): ToDoApi {
        return retrofit.create(ToDoApi::class.java)
    }
}