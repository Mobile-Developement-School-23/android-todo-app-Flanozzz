package com.example.todolist.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.Model.ToDoItem
import com.example.todolist.Repository.Repositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectedTaskViewModel(
    private val deviceId: String
) : ViewModel() {
    private var _selectedTaskLiveData = MutableLiveData(ToDoItem.getDefaultTask(deviceId))
    val selectedTaskLiveData: LiveData<ToDoItem> = _selectedTaskLiveData

    private var _isNewTaskLiveData = MutableLiveData(false)
    val isNewTaskLiveData: LiveData<Boolean> = _isNewTaskLiveData

    private val repository = Repositories.toDoNetworkRepository
    //private val repository = Repositories.toDoDbRepository

    fun selectTask(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            val selectedToDoItem = repository.getById(id)
            withContext(Dispatchers.Main){
                _isNewTaskLiveData.value = false
                _selectedTaskLiveData.value = selectedToDoItem
            }
        }
    }

    fun createTask(){
        _isNewTaskLiveData.value = true
        _selectedTaskLiveData.value = ToDoItem.getDefaultTask(deviceId)
    }

//    private fun getNewTask(): ToDoItem{
//        return ToDoItem(
//            id = UUID.randomUUID().toString(),
//            text = "",
//            importance = ToDoItem.Importance.DEFAULT,
//            deadline = null,
//            isDone = false,
//            dateOfCreate = getCurrentUnixTime(),
//            dateOfChange = getCurrentUnixTime(),
//            color = null,
//            lastUpdatedBy = "tmp"
//        )
//    }

    fun setTaskDeadline(newDeadline: Long){
        val oldTask = _selectedTaskLiveData.value
        if(oldTask != null){
            _selectedTaskLiveData.value = oldTask.copy(deadline = newDeadline)
        }
    }

    fun setTaskText(newText: String){
        val oldTask = _selectedTaskLiveData.value
        if(oldTask != null){
            _selectedTaskLiveData.value = oldTask.copy(text = newText)
        }
    }

    fun removeTaskDeadline(){
        val oldTask = _selectedTaskLiveData.value
        if(oldTask != null){
            _selectedTaskLiveData.value = oldTask.copy(deadline = null)
        }
    }

    fun setTaskImportance(newImportance: ToDoItem.Importance){
        val oldTask = _selectedTaskLiveData.value
        if(oldTask != null){
            _selectedTaskLiveData.value = oldTask.copy(importance = newImportance)
        }
    }

    fun saveTask(){
        viewModelScope.launch(Dispatchers.IO) {
            if(_isNewTaskLiveData.value!!){
                repository.addNewItem(_selectedTaskLiveData.value!!)
            }
            else{
                repository.changeItem(_selectedTaskLiveData.value!!)
            }
        }
    }

    fun deleteTask(){
        viewModelScope.launch(Dispatchers.IO) {
            if (_selectedTaskLiveData.value != null && !isNewTaskLiveData.value!!) {
                repository.deleteItem(_selectedTaskLiveData.value!!.id)
            }
        }
    }
}