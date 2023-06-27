package com.example.todolist

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.Repository.IToDoItemsRepository
import com.example.todolist.Repository.Repositories
import com.example.todolist.ViewModel.SelectedTaskViewModel
import com.example.todolist.ViewModel.ToDoListViewModel

open class ViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass){
            ToDoListViewModel::class.java -> {
                ToDoListViewModel()
            }
            SelectedTaskViewModel::class.java -> {
                SelectedTaskViewModel()
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}

//fun Fragment.myFactory() = ViewModelFactory(
//    toDoItemsRepository = Repositories.toDoDbRepository
//)
//
//fun Activity.myFactory() = ViewModelFactory(
//    toDoItemsRepository = Repositories.toDoDbRepository
//)