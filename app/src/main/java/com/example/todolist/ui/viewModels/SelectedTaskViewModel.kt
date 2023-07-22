package com.example.todolist.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.data.repository.IRepository
import com.example.todolist.utils.getCurrentUnixTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectedTaskViewModel(
    private val deviceId: String,
    private val repository: IRepository
) : ViewModel() {
    private var _selectedTaskFlow = MutableStateFlow(ToDoItem.getDefaultTask(deviceId))
    val selectedTaskFlow: Flow<ToDoItem> = _selectedTaskFlow

    private var _isNewTask = MutableStateFlow(true)
    val isNewTaskFlow: Flow<Boolean> = _isNewTask

    fun selectTask(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            val selectedToDoItem = repository.getById(id)
            withContext(Dispatchers.Main){
                if(selectedToDoItem != null){
                    _isNewTask.value = false
                    _selectedTaskFlow.value = selectedToDoItem
                }
                else{
                    createTask()
                }
            }
        }
    }

    fun createTask(){
        _isNewTask.value = true
        _selectedTaskFlow.value = ToDoItem.getDefaultTask(deviceId)
    }

//    fun isNewTask(): Boolean{
//        return isNewTask
//    }

    fun updateText(text: String){
        _selectedTaskFlow.value = _selectedTaskFlow.value.copy(
            text = text,
            dateOfChange = getCurrentUnixTime(),
            lastUpdatedBy = deviceId,
        )
    }

    fun updateDeadline(deadline: Long?){
        _selectedTaskFlow.value = _selectedTaskFlow.value.copy(
            deadline = deadline,
            dateOfChange = getCurrentUnixTime(),
            lastUpdatedBy = deviceId,
        )
    }

    fun updateImportance(importance: ToDoItem.Importance){
        _selectedTaskFlow.value = _selectedTaskFlow.value.copy(
            importance = importance,
            dateOfChange = getCurrentUnixTime(),
            lastUpdatedBy = deviceId,
        )
    }
}