package com.example.todolist.di.app

import android.content.Context
import androidx.room.Room
import com.example.todolist.data.db.ToDoDatabase
import com.example.todolist.data.db.ToDoItemDao
import com.example.todolist.di.scopes.AppScope
import com.example.todolist.utils.Constants
import dagger.Module
import dagger.Provides

@Module
object DbModule {
    @Provides
    @AppScope
    fun provideToDoItemDao(database: ToDoDatabase): ToDoItemDao {
        return database.toDoItemDao()
    }

    @Provides
    @AppScope
    fun provideToDoDatabase(context: Context): ToDoDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ToDoDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }
}