package com.example.todolist.di.activity

import com.example.todolist.di.fragment.EditTaskFragmentComponent
import com.example.todolist.di.fragment.ToDoListFragmentComponent
import com.example.todolist.di.scopes.ActivityScope
import com.example.todolist.ui.MainActivity
import dagger.Subcomponent

@Subcomponent
@ActivityScope
interface ActivityComponent {
    fun inject(mainActivity: MainActivity)
    fun editTaskFragmentComponent(): EditTaskFragmentComponent
    fun toDoListFragmentComponent(): ToDoListFragmentComponent

}
