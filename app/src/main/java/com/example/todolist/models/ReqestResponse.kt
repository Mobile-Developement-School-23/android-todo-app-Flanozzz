package com.example.todolist.models

//data class ListElement(
//    val id: String,
//    val text: String,
//    val importance: String,
//    val deadline: Long?,
//    val done: Boolean,
//    val color: String?,
//    val created_at: Long,
//    val changed_at: Long,
//    val last_updated_by: String
//)


data class ElementRequest(
    val element: ToDoItem
)

data class ElementResponse(
    val status: String,
    val element: ToDoItem,
    val revision: Int
)

data class ListRequest(
    val list: List<ToDoItem>
)

data class ListResponse(
    val status: String,
    val list: List<ToDoItem>,
    val revision: Int
)