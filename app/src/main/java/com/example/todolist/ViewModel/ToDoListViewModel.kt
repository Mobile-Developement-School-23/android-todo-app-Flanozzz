package com.example.todolist.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.Model.ToDoItem
import com.example.todolist.Repository.IToDoItemsRepository
import com.example.todolist.Repository.ToDoListListener

open class ToDoListViewModel(
    private val toDoItemsRepository: IToDoItemsRepository
) : ViewModel() {
    private val _toDoItemsLiveData: MutableLiveData<List<ToDoItem>> = MutableLiveData(emptyList())
    val toDoItemsLiveData: LiveData<List<ToDoItem>> = _toDoItemsLiveData

    private var showOnlyCompletedState = false

    private val toDoListListener: ToDoListListener = {
        _toDoItemsLiveData.value = it
    }

    init{
        toDoItemsRepository.addListener(toDoListListener)
    }

    fun getShowOnlyCompletedState() : Boolean{
        return showOnlyCompletedState
    }

    fun changeState(){
        showOnlyCompletedState = !showOnlyCompletedState
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