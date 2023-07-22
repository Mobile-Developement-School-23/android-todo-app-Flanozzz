package com.example.todolist.di.app

import android.content.Context
import android.content.SharedPreferences
import com.example.todolist.di.scopes.AppScope
import com.example.todolist.utils.Constants.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides

@Module
object AppModule {
    @Provides
    @AppScope
    fun provideSharedPreferences(
        context: Context
    ): SharedPreferences{
        return  context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
}