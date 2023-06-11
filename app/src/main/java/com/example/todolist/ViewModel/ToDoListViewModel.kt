package com.example.todolist.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.Model.ToDoItem
import com.example.todolist.Repository.IToDoItemsRepository
import com.example.todolist.Repository.ToDoListListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class ToDoListViewModel(
    private val toDoItemsRepository: IToDoItemsRepository
) : ViewModel() {
    private val _toDoItemsLiveData: MutableLiveData<List<ToDoItem>> = MutableLiveData(emptyList())
    val toDoItemsLiveData: LiveData<List<ToDoItem>> = _toDoItemsLiveData

    private val toDoListListener: ToDoListListener = {
        _toDoItemsLiveData.value = it
    }

    init{
        toDoItemsRepository.addListener(toDoListListener)
    }
}