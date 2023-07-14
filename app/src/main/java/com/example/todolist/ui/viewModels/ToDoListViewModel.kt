package com.example.todolist.ui.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.data.repository.IRepository
import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.data.source.network.NetworkSource
import com.example.todolist.data.source.network.UnsuccessfulResponseException
import com.example.todolist.data.workers.NotificationBroadcastReceiver
import com.example.todolist.data.workers.NotificationJobService
import com.example.todolist.data.workers.NotificationWorker
import com.example.todolist.data.workers.NotificationWorker.Companion.scheduleNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


open class ToDoListViewModel(
    private val repository: IRepository
) : ViewModel() {
    private var _toDoItems: MutableStateFlow<List<ToDoItem>> = MutableStateFlow(emptyList())
    val toDoItems: Flow<List<ToDoItem>> = _toDoItems

    private val _viewableTasksStateFlow: MutableStateFlow<List<ToDoItem>> = MutableStateFlow(_toDoItems.value)
    val viewableTasksStateFlow: Flow<List<ToDoItem>> = _viewableTasksStateFlow

    private val _showOnlyUnfinishedStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showOnlyUnfinishedStateFlow: Flow<Boolean> = _showOnlyUnfinishedStateFlow

    private val _isLastResponseSuccess: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLastResponseSuccess: Flow<Boolean> = _isLastResponseSuccess


    init {
        startCollectCoroutine()
    }

    private fun startCollectCoroutine() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getItems().collect { list ->
                withContext(Dispatchers.Main) {
                    _toDoItems.value = list
                    if (_showOnlyUnfinishedStateFlow.value) {
                        _viewableTasksStateFlow.value = list.filter { !it.isDone }
                    } else {
                        _viewableTasksStateFlow.value = list
                    }
                }
            }
        }
    }

    fun syncData(){
        viewModelScope.launch(Dispatchers.IO) {
            performActionAndSetStatus {
                repository.syncData()
            }
        }
    }

    fun changeShowOnlyUnfinishedState(){
        _showOnlyUnfinishedStateFlow.value = !_showOnlyUnfinishedStateFlow.value
    }

    fun setViewableTasksByState(state: Boolean){
        if(state){
            _viewableTasksStateFlow.value = _toDoItems.value.filter { !it.isDone }
        }
        else {
            _viewableTasksStateFlow.value = _toDoItems.value
        }
    }

    fun changeTask(task: ToDoItem){
        viewModelScope.launch(Dispatchers.IO) {
            performActionAndSetStatus {
                repository.changeItem(task)
            }
        }
    }

    fun deleteTask(task: ToDoItem){
        viewModelScope.launch(Dispatchers.IO) {
            performActionAndSetStatus {
                repository.deleteItem(task.id)
            }
        }
    }

    fun saveToDoItem(newToDoItem: ToDoItem, isNewTask: Boolean, context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            performActionAndSetStatus {
                if(isNewTask){
                    repository.addNewItem(newToDoItem)
                } else{
                    repository.changeItem(newToDoItem)
                }
                scheduleNotification(newToDoItem, context)
                //NotificationBroadcastReceiver.scheduleNotification(newToDoItem, context)
            }
        }
    }

    private suspend fun performActionAndSetStatus(action: suspend () -> Unit) {
        try {
            action()
            _isLastResponseSuccess.value = true
        } catch (e: UnsuccessfulResponseException) {
            _isLastResponseSuccess.value = false
        }
    }
}