package com.example.todolist.di

import android.content.Context
import com.example.todolist.data.repository.IRepository
import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.ui.viewModels.ViewModelFactory
import com.example.todolist.utils.getDeviceId
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @Provides
    @AppScope
    fun provideViewModelFactory(
        context: Context,
        repository: IRepository
    ): ViewModelFactory{
        return ViewModelFactory(getDeviceId(context), repository)
    }
}