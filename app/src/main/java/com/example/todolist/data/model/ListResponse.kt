package com.example.todolist.data.model

data class ListResponse(
    val status: String,
    val list: List<ToDoItem>,
    val revision: Int
)
