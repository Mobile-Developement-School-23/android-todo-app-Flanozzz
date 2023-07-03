package com.example.todolist.ui.adapter

import com.example.todolist.data.model.ToDoItem

interface IOnTaskTouchListener {
    fun onChangeButtonClick(id: String)
    fun onCheckboxClick(task: ToDoItem)
    fun onTaskDelete(task: ToDoItem)
}