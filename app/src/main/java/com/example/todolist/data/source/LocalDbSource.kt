package com.example.todolist.data.source

import com.example.todolist.data.model.ToDoItem
import com.example.todolist.data.db.ToDoItemDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDbSource @Inject constructor(private val toDoItemDao: ToDoItemDao) {

    suspend fun addNewItem(item: ToDoItem) {
        toDoItemDao.addToDoItem(item)
    }

    suspend fun addNewItems(items: List<ToDoItem>){
        toDoItemDao.addToDoItems(items)
    }

    suspend fun getItems(): Flow<List<ToDoItem>> = toDoItemDao.readAllToDoItems()

    suspend fun getById(id: String): ToDoItem? {
        return toDoItemDao.getToDoItemById(id)
    }

    suspend fun changeItem(toDoItem: ToDoItem) {
        toDoItemDao.updateToDoItem(toDoItem)
    }

    suspend fun deleteItem(id: String) {
        toDoItemDao.deleteToDoItem(id)
    }
}