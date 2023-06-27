package com.example.todolist.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.Model.ToDoItem
import com.example.todolist.Repository.IToDoItemsRepository
import com.example.todolist.Repository.Repositories
import com.example.todolist.Utils.getCurrentUnixTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectedTaskViewModel : ViewModel() {
    private var _selectedTaskLiveData = MutableLiveData(getNewTask())
    val selectedTaskLiveData: LiveData<ToDoItem> = _selectedTaskLiveData

    private var _isNewTaskLiveData = MutableLiveData(false)
    val isNewTaskLiveData: LiveData<Boolean> = _isNewTaskLiveData

    fun selectTask(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            val selectedToDoItem = Repositories.toDoDbRepository.getById(id)
            withContext(Dispatchers.Main){
                _isNewTaskLiveData.value = false
                _selectedTaskLiveData.value = selectedToDoItem
            }
        }
    }

    fun createTask(){
        _isNewTaskLiveData.value = true
        _selectedTaskLiveData.value = getNewTask()
    }

    private fun getNewTask(): ToDoItem{
        return ToDoItem(
            id = "tmpId_" + System.currentTimeMillis(),
            text = "",
            importance = ToDoItem.Importance.DEFAULT,
            deadline = 0,
            isDone = false,
            dateOfCreate = getCurrentUnixTime(),
            dateOfChange = getCurrentUnixTime(),
            color = null
        )
    }

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
        //_selectedTaskLiveData.value!!.setTaskText(newText)
    }

    fun removeTaskDeadline(){
        val oldTask = _selectedTaskLiveData.value
        if(oldTask != null){
            _selectedTaskLiveData.value = oldTask.copy(deadline = null)
        }
        //_selectedTaskLiveData.value!!.removeDeadline()
    }

    fun setTaskImportance(newImportance: ToDoItem.Importance){
        val oldTask = _selectedTaskLiveData.value
        if(oldTask != null){
            _selectedTaskLiveData.value = oldTask.copy(importance = newImportance)
        }
        //_selectedTaskLiveData.value!!.setImportance(importance)
    }

    fun saveTask(){
        viewModelScope.launch(Dispatchers.IO) {
            if(_isNewTaskLiveData.value!!){
                Repositories.toDoDbRepository.addNewItem(_selectedTaskLiveData.value!!)
            }
            else{
                Repositories.toDoDbRepository.changeItem(_selectedTaskLiveData.value!!)
            }
        }
    }

    fun deleteTask(){
        viewModelScope.launch(Dispatchers.IO) {
            if (_selectedTaskLiveData.value != null && !isNewTaskLiveData.value!!) {
                Repositories.toDoDbRepository.deleteItem(_selectedTaskLiveData.value!!.id)
            }
        }
    }
}