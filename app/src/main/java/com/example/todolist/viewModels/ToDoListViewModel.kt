package com.example.todolist.viewModels

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.models.ToDoItem
import com.example.todolist.repositories.Repositories
import com.example.todolist.repositories.ToDoNetworkRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



open class ToDoListViewModel : ViewModel() {
    private var toDoItems: List<ToDoItem> = emptyList()

    private val _viewableTasksStateFlow: MutableStateFlow<List<ToDoItem>> = MutableStateFlow(toDoItems)
    val viewableTasksStateFlow: Flow<List<ToDoItem>> = _viewableTasksStateFlow

    private val _showOnlyUnfinishedStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showOnlyUnfinishedStateFlow: Flow<Boolean> = _showOnlyUnfinishedStateFlow

    private val _repositoryRequestStatus: MutableStateFlow<ToDoNetworkRepository.ResponseStatus> =
        MutableStateFlow(ToDoNetworkRepository.ResponseStatus.Successful)
    val repositoryRequestStatus: Flow<ToDoNetworkRepository.ResponseStatus> = _repositoryRequestStatus

    private val repository = Repositories.toDoRepository

    init {
        startCollectCoroutine()
    }

    private fun startCollectCoroutine() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getItems().collect { list ->
                withContext(Dispatchers.Main) {
                    toDoItems = list
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
            _repositoryRequestStatus.value = repository.syncData()
        }
    }

    fun changeShowOnlyUnfinishedState(){
        _showOnlyUnfinishedStateFlow.value = !_showOnlyUnfinishedStateFlow.value
    }

    fun setViewableTasksByState(state: Boolean){
        if(state){
            _viewableTasksStateFlow.value = toDoItems.filter { !it.isDone }
        }
        else {
            _viewableTasksStateFlow.value = toDoItems
        }
    }

    fun changeTask(task: ToDoItem){
        viewModelScope.launch(Dispatchers.IO) {
            _repositoryRequestStatus.value = repository.changeItem(task)
        }
    }

    fun deleteTask(task: ToDoItem){
        viewModelScope.launch(Dispatchers.IO) {
            _repositoryRequestStatus.value = repository.deleteItem(task.id)
        }
    }
}