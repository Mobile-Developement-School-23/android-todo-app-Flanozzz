package com.example.todolist.di.fragment

import com.example.todolist.di.scopes.FragmentScope
import com.example.todolist.ui.screens.EditTaskScreen.EditTaskFragment
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface EditTaskFragmentComponent {
    fun inject(editTaskFragment: EditTaskFragment)
}