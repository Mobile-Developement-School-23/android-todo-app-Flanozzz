package com.example.todolist

import com.example.todolist.Model.ToDoItem

interface IOnTaskTouchListener {
    fun onChangeButtonClick(id: String)
    fun onCheckboxClick(task: ToDoItem, state: Boolean)
    fun onTaskMove(task: ToDoItem, moveBy: Int)
    fun onTaskDelete(task: ToDoItem)
}