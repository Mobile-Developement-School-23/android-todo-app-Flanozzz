package com.example.todolist.ui.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.data.repository.IRepository
import com.example.todolist.di.scopes.ActivityScope
import com.example.todolist.ui.notifications.NotificationScheduler
import com.example.todolist.utils.getDeviceId
import javax.inject.Inject

@ActivityScope
open class ViewModelFactory @Inject constructor(
    private val context: Context,
    private val repository: IRepository,
    private val notificationScheduler: NotificationScheduler
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass){
            SelectedTaskViewModel::class.java -> {
                SelectedTaskViewModel(getDeviceId(context), repository)
            }
            ToDoListViewModel::class.java -> {
                ToDoListViewModel(repository, notificationScheduler)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}
