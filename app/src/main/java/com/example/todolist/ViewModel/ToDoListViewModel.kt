package com.example.todolist.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.Model.ToDoItem
import com.example.todolist.Repository.IToDoItemsRepository
import com.example.todolist.Repository.ToDoListListener
import com.example.todolist.Repository.ToDoNetworkRepository
import com.example.todolist.Utils.getCurrentUnixTime
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class ToDoListViewModel(
    private val toDoItemsRepository: IToDoItemsRepository
) : ViewModel() {
    private val _toDoItemsLiveData: MutableLiveData<List<ToDoItem>> = MutableLiveData(emptyList())
    val toDoItemsLiveData: LiveData<List<ToDoItem>> = _toDoItemsLiveData

    private val _viewableTasks: MutableLiveData<List<ToDoItem>> = MutableLiveData(_toDoItemsLiveData.value)
    val viewableTasks: LiveData<List<ToDoItem>> = _viewableTasks

    private val _showOnlyUnfinishedStateLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val showOnlyUnfinishedStateLiveData: LiveData<Boolean> = _showOnlyUnfinishedStateLiveData

    init{
        viewModelScope.launch {
            toDoItemsRepository.getItems().collect{list ->
                _toDoItemsLiveData.value = list
                if(_showOnlyUnfinishedStateLiveData.value != null){
                    if(_showOnlyUnfinishedStateLiveData.value!!){
                        _viewableTasks.value = list.filter { !it.isDone() }
                    }
                    else {
                        _viewableTasks.value = list
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

    fun showTasksByState(state: Boolean){
        if(_toDoItemsLiveData.value != null){
            if(state){
                _viewableTasks.value = _toDoItemsLiveData.value!!.filter { !it.isDone() }
            }
            else {
                _viewableTasks.value = _toDoItemsLiveData.value
            }
        }
    }

    fun changeTask(task: ToDoItem, notifyListeners: Boolean){
        toDoItemsRepository.changeItem(task, notifyListeners)
    }

    fun moveTask(task: ToDoItem, moveBy: Int){
        toDoItemsRepository.moveItem(task, moveBy)
    }

    fun deleteTask(task: ToDoItem){
        toDoItemsRepository.deleteItem(task.getId())
    }

    fun getCompletedTasksCount() : Int{
        return _toDoItemsLiveData.value!!.filter { it.isDone() }.size
    }

    fun getTotalTasksCount(): Int{
        return _toDoItemsLiveData.value!!.size
    }
}