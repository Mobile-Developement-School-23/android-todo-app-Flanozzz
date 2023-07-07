package com.example.todolist.di.activity

import com.example.todolist.di.scopes.ActivityScope
import com.example.todolist.ui.MainActivity
import dagger.Subcomponent

@Subcomponent
@ActivityScope
interface ActivityComponent {
    fun inject(mainActivity: MainActivity)
}