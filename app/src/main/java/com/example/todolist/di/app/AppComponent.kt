package com.example.todolist.di.app

import android.content.Context
import com.example.todolist.data.DataSyncWorker
import com.example.todolist.di.activity.ActivityComponent
import com.example.todolist.di.scopes.AppScope
import dagger.BindsInstance
import dagger.Component

@Component(modules = [NetworkModule::class, RepositoryModule::class, DbModule::class])
@AppScope
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }

    fun activityComponent(): ActivityComponent

    fun inject(dataSyncWorker: DataSyncWorker)
}