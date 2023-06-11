package com.example.todolist.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.Model.ToDoItem
import com.example.todolist.Repository.IToDoItemsRepository

class SelectedTaskViewModel(
    private val toDoItemsRepository: IToDoItemsRepository
) : ViewModel() {
    private var _selectedTaskLiveData = MutableLiveData<ToDoItem>()
    val selectedTaskLiveData: LiveData<ToDoItem> = _selectedTaskLiveData

    private var _isNewTaskLiveData = MutableLiveData(false)
    val isNewTaskLiveData: LiveData<Boolean> = _isNewTaskLiveData

    fun selectTask(id: String){
        _isNewTaskLiveData.value = false
        _selectedTaskLiveData.value = toDoItemsRepository.getById(id)
    }

    fun createTask(){
        _isNewTaskLiveData.value = true
        _selectedTaskLiveData.value = ToDoItem(
            id = "tmpId_" + System.currentTimeMillis(),
            taskText = "",
            importance = ToDoItem.Importance.DEFAULT,
            deadline = 0,
            isDone = false,
            dateOfCreate = System.currentTimeMillis(),
            dateOfChange = System.currentTimeMillis(),
            hasDeadline = false
        )
    }

    fun saveTask(task: ToDoItem){
        _selectedTaskLiveData.value = task
        if(_isNewTaskLiveData.value!!){
            toDoItemsRepository.addNewItem(task)
        }
        else{
            toDoItemsRepository.changeItem(task)
        }
    }

    fun deleteTask(){
        if(_selectedTaskLiveData.value != null && !isNewTaskLiveData.value!!){
            toDoItemsRepository.deleteItem(_selectedTaskLiveData.value!!.getId())
        }
    }
}