package com.example.todolist.viewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.models.ToDoItem
import com.example.todolist.repositories.Repositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectedTaskViewModel(
    private val deviceId: String
) : ViewModel() {
    private var _selectedTaskFlow = MutableStateFlow(ToDoItem.getDefaultTask(deviceId))
    val selectedTaskFlow: Flow<ToDoItem> = _selectedTaskFlow

    private var isNewTask = false
    private val repository = Repositories.toDoRepository

    fun selectTask(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            val selectedToDoItem = repository.getById(id)
            withContext(Dispatchers.Main){
                if(selectedToDoItem != null){
                    isNewTask = false
                    _selectedTaskFlow.value = selectedToDoItem
                }
                else{
                    createTask()
                }
            }
        }
    }

    fun clearSelectedTask(){
        _selectedTaskFlow.value = ToDoItem.getDefaultTask(deviceId)
    }

    fun createTask(){
        isNewTask = true
        _selectedTaskFlow.value = ToDoItem.getDefaultTask(deviceId)
    }

    fun isNewTask(): Boolean{
        return isNewTask
    }

    fun removeTaskDeadline(){
        _selectedTaskFlow.value = _selectedTaskFlow.value.copy(deadline = null)
    }

    fun saveTask(_importance: ToDoItem.Importance, _deadline: Long?, _text: String){
        val newToDoItem = _selectedTaskFlow.value.copy(
            importance = _importance,
            deadline = _deadline,
            text = _text
        )
        viewModelScope.launch(Dispatchers.IO) {
            if(isNewTask){
                repository.addNewItem(newToDoItem)
            }
            else{
                repository.changeItem(newToDoItem)
            }
        }
    }

    fun deleteTask(){
        viewModelScope.launch(Dispatchers.IO) {
            if (!isNewTask) {
                repository.deleteItem(_selectedTaskFlow.value.id)
            }
        }
    }
}