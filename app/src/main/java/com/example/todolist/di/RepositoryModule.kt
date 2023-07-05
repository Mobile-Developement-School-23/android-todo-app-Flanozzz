package com.example.todolist.di

import com.example.todolist.data.repository.IRepository
import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.data.source.LocalDbSource
import com.example.todolist.data.source.network.NetworkSource
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface RepositoryModule {
    @Binds
    fun repo(impl: ToDoRepository): IRepository
}
