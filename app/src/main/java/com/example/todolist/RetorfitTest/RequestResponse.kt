package com.example.todolist.RetorfitTest

import com.example.todolist.Model.ListElement

data class ListRequest(
    val list: List<ListElement>,
    val revision: Int
)

data class ListResponse(
    val status: String,
    val list: List<ListElement>,
    val revision: Int
)
