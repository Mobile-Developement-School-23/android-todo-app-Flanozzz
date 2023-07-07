package com.example.todolist.di.app

import android.content.Context
import com.example.todolist.data.repository.IRepository
import com.example.todolist.di.scopes.AppScope
import com.example.todolist.ui.viewModels.ViewModelFactory
import com.example.todolist.utils.getDeviceId
import dagger.Module
import dagger.Provides

@Module
interface AppModule {
//    @Provides
//    @AppScope
//    fun provideViewModelFactory(
//        context: Context,
//        repository: IRepository
//    ): ViewModelFactory{
//        return ViewModelFactory(context, repository)
//    }
}