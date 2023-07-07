package com.example.todolist

import com.example.todolist.models.ToDoItem

interface IOnTaskTouchListener {
    fun onChangeButtonClick(id: String)
    fun onCheckboxClick(task: ToDoItem)
    fun onTaskDelete(task: ToDoItem)
}