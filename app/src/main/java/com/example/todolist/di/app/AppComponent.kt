package com.example.todolist.di.app

import android.content.Context
import com.example.todolist.data.DataSyncWorker
import com.example.todolist.di.activity.ActivityComponent
import com.example.todolist.di.fragment.EditTaskFragmentComponent
import com.example.todolist.di.fragment.ToDoListFragmentComponent
import com.example.todolist.di.scopes.AppScope
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class, NetworkModule::class, RepositoryModule::class, DbModule::class])
@AppScope
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }

    fun activityComponent(): ActivityComponent
    fun editTaskFragmentComponent(): EditTaskFragmentComponent
    fun toDoListFragmentComponent(): ToDoListFragmentComponent

    fun inject(dataSyncWorker: DataSyncWorker)
}