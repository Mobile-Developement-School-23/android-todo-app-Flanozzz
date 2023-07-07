package com.example.todolist.di

import android.content.Context
import com.example.todolist.di.subcomponents.ActivityComponent
import com.example.todolist.ui.MainActivity
import com.example.todolist.ui.fragments.EditTaskFragment
import com.example.todolist.ui.fragments.ToDoListFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class, NetworkModule::class, RepositoryModule::class, DbModule::class])
@AppScope
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun withContext(@BindsInstance context: Context): Builder
        fun build(): AppComponent
    }

    fun activityComponent(): ActivityComponent

    fun inject(editTaskFragment: EditTaskFragment)
    fun inject(toDoListFragment: ToDoListFragment)
//    fun inject(mainActivity: MainActivity)
}