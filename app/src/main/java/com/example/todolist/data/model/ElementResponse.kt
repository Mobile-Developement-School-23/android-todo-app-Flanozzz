package com.example.todolist.data.model

data class ElementResponse(
    val status: String,
    val element: ToDoItem,
    val revision: Int
)