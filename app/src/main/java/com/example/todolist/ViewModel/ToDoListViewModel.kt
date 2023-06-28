package com.example.todolist.ViewModel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.Model.ToDoItem
import com.example.todolist.Repository.Repositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

open class ToDoListViewModel : ViewModel() {
    private val _toDoItemsLiveData: MutableLiveData<List<ToDoItem>> = MutableLiveData(ArrayList())
    val toDoItemsLiveData: LiveData<List<ToDoItem>> = _toDoItemsLiveData

    private val _viewableTasks: MutableLiveData<List<ToDoItem>> = MutableLiveData(_toDoItemsLiveData.value)
    val viewableTasks: LiveData<List<ToDoItem>> = _viewableTasks

    private val _showOnlyUnfinishedStateLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val showOnlyUnfinishedStateLiveData: LiveData<Boolean> = _showOnlyUnfinishedStateLiveData

    private val repository = Repositories.toDoNetworkRepository
    //private val repository = Repositories.toDoDbRepository

    private var collectJob: Job? = null

    private fun startCollectCoroutine() {
        collectJob = viewModelScope.launch(Dispatchers.IO) {
            repository.getItems().collect { list ->
                withContext(Dispatchers.Main) {
                    _toDoItemsLiveData.value = list
                    if (_showOnlyUnfinishedStateLiveData.value != null) {
                        if (_showOnlyUnfinishedStateLiveData.value!!) {
                            _viewableTasks.value = list.filter { !it.isDone }
                        } else {
                            _viewableTasks.value = list
                        }
                    }
                }
            }
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
        if(_showOnlyUnfinishedStateLiveData.value != null){
            _showOnlyUnfinishedStateLiveData.value = !_showOnlyUnfinishedStateLiveData.value!!
        }
    }

    fun setViewableTasksByState(state: Boolean){
        if(_toDoItemsLiveData.value != null){
            if(state){
                _viewableTasks.value = _toDoItemsLiveData.value!!.filter { !it.isDone }
            }
            else {
                _viewableTasks.value = _toDoItemsLiveData.value
            }
        }
    }

    fun changeTask(task: ToDoItem, errorToast: Toast){
//        if(_toDoItemsLiveData.value != null){
//            val i = _toDoItemsLiveData.value!!.indexOfFirst { it.id == task.id }
//            val updateList = _toDoItemsLiveData.value!! as MutableList<ToDoItem>
//            updateList[i] = task
//            _toDoItemsLiveData.value = updateList
//        }
//        _toDoItemsLiveData.value!!.forEach {
//            Log.d("AAA", it.isDone.toString())
//        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.changeItem(task)
            }
            catch (e: RuntimeException){
                errorToast.show()
            }
        }
    }

    fun moveTask(task: ToDoItem, moveBy: Int){
        repository.moveItem(task, moveBy)
    }

    fun deleteTask(task: ToDoItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(task.id)
        }
    }




//    fun getCompletedTasksCount() : Int{
//        return _toDoItemsLiveData.value!!.filter { it.isDone }.size
//    }
//
//    fun getTotalTasksCount(): Int{
//        return _toDoItemsLiveData.value!!.size
//    }
}