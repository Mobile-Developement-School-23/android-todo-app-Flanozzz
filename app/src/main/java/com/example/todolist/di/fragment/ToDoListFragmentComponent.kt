package com.example.todolist.di.fragment

import com.example.todolist.di.scopes.FragmentScope
import com.example.todolist.ui.screens.TaskListScreen.ToDoListFragment
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface ToDoListFragmentComponent {
    fun inject(toDoListFragment: ToDoListFragment)
}