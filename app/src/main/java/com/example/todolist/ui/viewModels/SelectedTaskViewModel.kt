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

    private var isNewTask = false

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

    fun createTask(){
        isNewTask = true
        _selectedTaskFlow.value = ToDoItem.getDefaultTask(deviceId)
    }

    fun isNewTask(): Boolean{
        return isNewTask
    }

    fun setNewData(newImportance: ToDoItem.Importance, newDeadline: Long?, newText: String){
        _selectedTaskFlow.value = _selectedTaskFlow.value.copy(
            text = newText,
            deadline = newDeadline,
            importance = newImportance,
            dateOfChange = getCurrentUnixTime(),
            lastUpdatedBy = deviceId,
        )
    }
}