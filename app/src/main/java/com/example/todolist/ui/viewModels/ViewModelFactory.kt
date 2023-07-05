package com.example.todolist.ui.viewModels

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.data.repository.IRepository
import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.utils.getDeviceId

open class ViewModelFactory(
    private val deviceId: String,
    private val repository: IRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass){
            SelectedTaskViewModel::class.java -> {
                SelectedTaskViewModel(deviceId, repository)
            }
            ToDoListViewModel::class.java -> {
                ToDoListViewModel(repository)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}

//fun Fragment.viewModelFactory() = ViewModelFactory(
//    deviceId = getDeviceId(requireContext()),
//    repository =
//)
//
//fun Activity.viewModelFactory() = ViewModelFactory(
//    deviceId = getDeviceId(this)
//)