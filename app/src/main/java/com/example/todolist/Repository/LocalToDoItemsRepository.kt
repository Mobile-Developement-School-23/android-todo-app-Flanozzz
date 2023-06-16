package com.example.todolist.Repository

import android.util.Log
import com.example.todolist.Model.ToDoItem
import java.util.*
import kotlin.collections.ArrayList

typealias ToDoListListener = (toDoList: List<ToDoItem>) -> Unit

class LocalToDoItemsRepository : IToDoItemsRepository {

    private var toDoItems: ArrayList<ToDoItem> = items
    private val listeners = mutableSetOf<ToDoListListener>()

    init {
        notifyListeners()
    }

    override fun addNewItem(item: ToDoItem) {
        toDoItems.add(item)
        notifyListeners()
    }

    override fun getItems(): ArrayList<ToDoItem> {
        return toDoItems
    }

    override fun addListener(listener: ToDoListListener){
        listeners.add(listener)
        notifyListeners()
    }

    override fun removeListener(listener: ToDoListListener){
        listeners.remove(listener)
    }

    private fun notifyListeners(){
        listeners.forEach{it.invoke(toDoItems)}
    }

    override fun getById(id: String) : ToDoItem{
        val index = getIndexById(id)
        return toDoItems[index]
    }

    override fun changeItem(task: ToDoItem, notifyListeners: Boolean){
        val index = getIndexById(task.getId())
        toDoItems[index] = task
        notifyListeners()
    }

    override fun deleteItem(id: String) {
        val index = getIndexById(id)
        if (index == -1) return
        toDoItems = ArrayList(toDoItems)
        toDoItems.removeAt(index)
        notifyListeners()
    }

    override fun moveItem(task: ToDoItem, moveBy: Int) {
        val oldIndex = getIndexById(task.getId())
        if(oldIndex == -1) return
        val newIndex = oldIndex + moveBy
        if(newIndex < 0 || newIndex >= items.size) return
        toDoItems = ArrayList(toDoItems)
        Collections.swap(toDoItems, oldIndex, newIndex)
        notifyListeners()
    }

    override fun getCompletedTasksCount(): Int{
        var count = 0
        toDoItems.forEach {
            if(it.isDone()) count++
        }
        return count
    }

    override fun getCompletedTasks(): ArrayList<ToDoItem> {
        return ArrayList(toDoItems.filter { it.isDone() })
    }

    private fun getIndexById(id: String): Int{
        return toDoItems.indexOfFirst { it.getId() == id }
    }

    companion object{
        private val items = arrayListOf(
            ToDoItem("1e", "Купить что-то", ToDoItem.Importance.HIGH, 1691836648, false, 0, 0, true),
            ToDoItem("2r", "Купить что-то", ToDoItem.Importance.DEFAULT, 0, false, 0, 0, false),
            ToDoItem("3t", "Купить что-то, где-то, зачем-то, но зачем не очень понятно", ToDoItem.Importance.DEFAULT, 0, false, 0, 0, false),
            ToDoItem("6y", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрррррррррррррррррррррррррр", ToDoItem.Importance.DEFAULT, 1691836648, true, 0, 0, true),
            ToDoItem("7", "Купить что-то", ToDoItem.Importance.DEFAULT, 0, false, 0, 0, false),
            ToDoItem("8", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрррррррррррррррррррррррррр", ToDoItem.Importance.HIGH, 0, true, 0, 0, false),
            ToDoItem("9", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрррррррррррррррррррррррррр", ToDoItem.Importance.LOW, 0, false, 0, 0, false),
            ToDoItem("q", "Купить что-то", ToDoItem.Importance.DEFAULT, 0, true, 0, 0, false),
            ToDoItem("w", "Купить что-то", ToDoItem.Importance.DEFAULT, 0, false, 0, 0, false),
            ToDoItem("e", "Купить что-то", ToDoItem.Importance.HIGH, 0, true, 0, 0, false),
            ToDoItem("r", "Купить что-то", ToDoItem.Importance.DEFAULT, 0, false, 0, 0, false),
            ToDoItem("t", "Купить что-то", ToDoItem.Importance.HIGH, 0, true, 0, 0, false),
            ToDoItem("y", "Купить что-то", ToDoItem.Importance.DEFAULT, 0, false, 0, 0, false),
            ToDoItem("u", "Купить что-то", ToDoItem.Importance.DEFAULT, 0, true, 0, 0, false),
            ToDoItem("i", "Купить что-то", ToDoItem.Importance.LOW, 0, false, 0, 0, false),
            ToDoItem("o", "Купить что-то", ToDoItem.Importance.DEFAULT, 0, true, 0, 0, false),
        )
    }
}