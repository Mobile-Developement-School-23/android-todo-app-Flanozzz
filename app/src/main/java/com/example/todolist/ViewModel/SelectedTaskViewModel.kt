package com.example.todolist.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.Model.ToDoItem
import com.example.todolist.Repository.IToDoItemsRepository
import com.example.todolist.Utils.getCurrentUnixTime
import com.example.todolist.Utils.getUnixTime

class SelectedTaskViewModel(
    private val toDoItemsRepository: IToDoItemsRepository
) : ViewModel() {
    private var _selectedTaskLiveData = MutableLiveData(getNewTask())
    val selectedTaskLiveData: LiveData<ToDoItem> = _selectedTaskLiveData

    private var _isNewTaskLiveData = MutableLiveData(false)
    val isNewTaskLiveData: LiveData<Boolean> = _isNewTaskLiveData

    fun selectTask(id: String){
        _isNewTaskLiveData.value = false
        _selectedTaskLiveData.value = toDoItemsRepository.getById(id)
    }

    fun createTask(){
        _isNewTaskLiveData.value = true
        _selectedTaskLiveData.value = getNewTask()
    }

    private fun getNewTask(): ToDoItem{
        return ToDoItem(
            id = "tmpId_" + System.currentTimeMillis(),
            taskText = "",
            importance = ToDoItem.Importance.DEFAULT,
            deadline = 0,
            isDone = false,
            dateOfCreate = getCurrentUnixTime(),
            dateOfChange = getCurrentUnixTime(),
            hasDeadline = false
        )
    }

    fun setTaskDeadline(deadline: Long){
        _selectedTaskLiveData.value!!.setDeadline(deadline)
    }

    fun setTaskText(text: String){
        _selectedTaskLiveData.value!!.setTaskText(text)
    }

    fun removeTaskDeadline(){
        _selectedTaskLiveData.value!!.removeDeadline()
    }

    fun setTaskImportance(importance: ToDoItem.Importance){
        _selectedTaskLiveData.value!!.setImportance(importance)
    }

    fun saveTask(){
        if(_isNewTaskLiveData.value!!){
            toDoItemsRepository.addToBegin(_selectedTaskLiveData.value!!)
        }
        else{
            toDoItemsRepository.changeItem(_selectedTaskLiveData.value!!)
        }
    }

    fun deleteTask(){
        if(_selectedTaskLiveData.value != null && !isNewTaskLiveData.value!!){
            toDoItemsRepository.deleteItem(_selectedTaskLiveData.value!!.getId())
        }
    }
}