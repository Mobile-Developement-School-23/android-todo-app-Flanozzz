package com.example.todolist.di.app

import com.example.todolist.data.repository.IRepository
import com.example.todolist.data.repository.ToDoRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @Binds
    fun bindRepository(impl: ToDoRepository): IRepository
}
