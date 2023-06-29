package com.example.todolist.viewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.models.ToDoItem
import com.example.todolist.repositories.Repositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



open class ToDoListViewModel : ViewModel() {
    private var toDoItems: List<ToDoItem> = emptyList()

    private val _viewableTasks: MutableStateFlow<List<ToDoItem>> = MutableStateFlow(toDoItems)
    val viewableTasks: Flow<List<ToDoItem>> = _viewableTasks

    private val _showOnlyUnfinishedStateLiveData: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showOnlyUnfinishedStateLiveData: Flow<Boolean> = _showOnlyUnfinishedStateLiveData

    private val repository = Repositories.toDoRepository

    private var collectJob: Job? = null

    private fun startCollectCoroutine() {
        Log.w("AAA", "start collect")
        viewModelScope.launch(Dispatchers.IO) {
            repository.getItems().collect { list ->
                withContext(Dispatchers.Main) {
                    Log.w("AAA", "collect")
                    toDoItems = list
                    if (_showOnlyUnfinishedStateLiveData.value) {
                        _viewableTasks.value = list.filter { !it.isDone }
                    } else {
                        _viewableTasks.value = list
                    }
                }
            }
        }
    }

    fun syncData(){
        viewModelScope.launch(Dispatchers.IO) {
            Log.w("AAA", "syncData")
            repository.syncData()
        }
    }

    fun restartCollectCoroutine() {
        collectJob?.cancel()
        startCollectCoroutine()
    }

    fun cancelCollectCoroutine() {
        collectJob?.cancel()
        collectJob = null
    }

    fun changeShowOnlyUnfinishedState(){
        _showOnlyUnfinishedStateLiveData.value = !_showOnlyUnfinishedStateLiveData.value!!
    }

    fun setViewableTasksByState(state: Boolean){
        if(state){
            _viewableTasks.value = toDoItems.filter { !it.isDone }
        }
        else {
            _viewableTasks.value = toDoItems
        }
    }

    fun changeTask(task: ToDoItem, errToast: Toast){
        viewModelScope.launch(Dispatchers.IO) {
            val success = repository.changeItem(task)
            if (!success) errToast.show()
        }
    }

    fun deleteTask(task: ToDoItem, errToast: Toast){
        viewModelScope.launch(Dispatchers.IO) {
            val success = repository.deleteItem(task.id)
            if (!success) errToast.show()
        }
    }
}