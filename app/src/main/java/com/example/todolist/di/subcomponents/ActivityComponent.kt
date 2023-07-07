package com.example.todolist.di.subcomponents

import com.example.todolist.ui.MainActivity
import dagger.Subcomponent

@Subcomponent
interface ActivityComponent {
    fun inject(mainActivity: MainActivity)
}