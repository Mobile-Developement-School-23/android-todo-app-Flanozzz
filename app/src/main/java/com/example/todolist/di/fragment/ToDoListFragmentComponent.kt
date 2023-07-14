package com.example.todolist.di.fragment

import com.example.todolist.di.scopes.FragmentScope
import com.example.todolist.ui.adapter.IOnTaskTouchListener
import com.example.todolist.ui.adapter.ToDoListAdapter
import com.example.todolist.ui.fragments.ToDoListFragment
import com.example.todolist.ui.viewModels.ViewModelFactory
import com.example.todolist.ui.viewModels.ViewModelFactory_Factory
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface ToDoListFragmentComponent {
    fun inject(toDoListFragment: ToDoListFragment)
}