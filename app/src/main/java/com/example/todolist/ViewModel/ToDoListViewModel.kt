package com.example.todolist.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.Model.ToDoItem
import com.example.todolist.Repository.Repositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class ToDoListViewModel : ViewModel() {
    private val _toDoItemsLiveData: MutableLiveData<ArrayList<ToDoItem>> = MutableLiveData(ArrayList())
    val toDoItemsLiveData: LiveData<ArrayList<ToDoItem>> = _toDoItemsLiveData

    private val _viewableTasks: MutableLiveData<List<ToDoItem>> = MutableLiveData(_toDoItemsLiveData.value)
    val viewableTasks: LiveData<List<ToDoItem>> = _viewableTasks

    private val _showOnlyUnfinishedStateLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val showOnlyUnfinishedStateLiveData: LiveData<Boolean> = _showOnlyUnfinishedStateLiveData

    private val _completedTasksCount: MutableLiveData<Int> = MutableLiveData(0)
    private val completedTasksCount: LiveData<Int> = _completedTasksCount

    init{
        launchCollectCoroutine()
    }

    private fun launchCollectCoroutine(){
        viewModelScope.launch(Dispatchers.IO) {
            Repositories.toDoDbRepository.getItems().collect{ list ->
                withContext(Dispatchers.Main){
                    _toDoItemsLiveData.value = list as ArrayList<ToDoItem>
                    if(_showOnlyUnfinishedStateLiveData.value != null){
                        if(_showOnlyUnfinishedStateLiveData.value!!){
                            _viewableTasks.value = list.filter { !it.isDone }
                        }
                        else {
                            _viewableTasks.value = list
                        }
                    }
                }
            }
        }
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

    fun changeTask(task: ToDoItem){
        if(_toDoItemsLiveData.value != null){
            val i = _toDoItemsLiveData.value!!.indexOfFirst { it.id == task.id }
            val updateList = _toDoItemsLiveData.value!!
            updateList[i] = task
            _toDoItemsLiveData.value = updateList
        }
//        _toDoItemsLiveData.value!!.forEach {
//            Log.d("AAA", it.isDone.toString())
//        }
//        viewModelScope.launch(Dispatchers.IO) {
//            //Repositories.toDoDbRepository.changeItem(task)
//        }
    }

    fun saveTasks(){
        viewModelScope.launch(Dispatchers.IO) {
            _toDoItemsLiveData.value?.forEach {
                Repositories.toDoDbRepository.changeItem(it)
            }
        }
    }

    fun moveTask(task: ToDoItem, moveBy: Int){
        Repositories.toDoDbRepository.moveItem(task, moveBy)
    }

    fun deleteTask(task: ToDoItem){
        viewModelScope.launch(Dispatchers.IO) {
            Repositories.toDoDbRepository.deleteItem(task.id)
        }
    }

    fun getCompletedTasksCount() : Int{
        return _toDoItemsLiveData.value!!.filter { it.isDone }.size
    }

    fun getTotalTasksCount(): Int{
        return _toDoItemsLiveData.value!!.size
    }
}