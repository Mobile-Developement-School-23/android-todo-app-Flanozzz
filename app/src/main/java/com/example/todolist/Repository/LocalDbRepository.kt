package com.example.todolist.Repository

import com.example.todolist.Model.ToDoItem
import com.example.todolist.Model.ToDoItemDao
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Collections

class LocalDbRepository(private val toDoItemDao: ToDoItemDao): IToDoItemsRepository {

    private val _toDoItemsFlow: MutableStateFlow<List<ToDoItem>> by lazy {
        MutableStateFlow(toDoItemDao.readAllToDoItems())
    }

    override suspend fun addNewItem(item: ToDoItem) {
        val updatedItems = _toDoItemsFlow.value.toMutableList()
        toDoItemDao.addToDoItem(item)
        updatedItems.add(item)
        _toDoItemsFlow.value = updatedItems
    }

//    override fun addToBegin(item: ToDoItem) {
//        val updatedItems = toDoItemsFlow.value.toMutableList()
//        updatedItems.add(0, item)
//        toDoItemsFlow.value = updatedItems
//    }

    override suspend fun getItems(): MutableStateFlow<List<ToDoItem>> = _toDoItemsFlow

    override suspend fun getById(id: String): ToDoItem? {
        return _toDoItemsFlow.value.find { it.id == id }
    }

    override suspend fun changeItem(task: ToDoItem) {
        val updatedItems = _toDoItemsFlow.value.toMutableList()
        val index = getIndexById(task.id)
        if (index != -1) {
            updatedItems[index] = task
            toDoItemDao.updateToDoItem(task)
            _toDoItemsFlow.value = updatedItems
        }
    }

    override suspend fun deleteItem(id: String) {
        val updatedItems = _toDoItemsFlow.value.toMutableList()
        val index = getIndexById(id)
        if (index != -1) {
            toDoItemDao.deleteToDoItem(updatedItems[index])
            updatedItems.removeAt(index)
            _toDoItemsFlow.value = updatedItems
        }
    }

    override fun moveItem(task: ToDoItem, moveBy: Int) {
        val oldIndex = getIndexById(task.id)
        if(oldIndex == -1) return
        val newIndex = oldIndex + moveBy
        if(newIndex < 0 || newIndex >= _toDoItemsFlow.value.size) return
        val updatedItems = _toDoItemsFlow.value.toMutableList()
        Collections.swap(updatedItems, oldIndex, newIndex)
        _toDoItemsFlow.value = updatedItems
    }

//    override fun getCompletedTasksCount(): Int{
//        var count = 0
//        toDoItemsFlow.value.forEach {
//            if(it.isDone) count++
//        }
//        return count
//    }
//
//    override fun getCompletedTasks(): ArrayList<ToDoItem> {
//        return ArrayList(toDoItemsFlow.value.filter { it.isDone })
//    }

    private fun getIndexById(id: String): Int{
        return _toDoItemsFlow.value.indexOfFirst { it.id == id }
    }
}