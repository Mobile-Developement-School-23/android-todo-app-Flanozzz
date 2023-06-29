package com.example.todolist.repositories

import com.example.todolist.models.ToDoItem
import com.example.todolist.models.db.ToDoItemDao
import kotlinx.coroutines.flow.Flow

class LocalDbRepository(private val toDoItemDao: ToDoItemDao): IToDoItemsRepository {

    override suspend fun addNewItem(item: ToDoItem) {
        toDoItemDao.addToDoItem(item)
    }

    suspend fun addNewItems(items: List<ToDoItem>){
        toDoItemDao.addToDoItems(items)
    }

    override suspend fun getItems(): Flow<List<ToDoItem>> = toDoItemDao.readAllToDoItems()

    override suspend fun getById(id: String): ToDoItem? {
        return toDoItemDao.getToDoItemById(id)
    }

    override suspend fun changeItem(toDoItem: ToDoItem) {
        toDoItemDao.updateToDoItem(toDoItem)
    }

    override suspend fun deleteItem(id: String) {
        toDoItemDao.deleteToDoItem(id)
    }
}