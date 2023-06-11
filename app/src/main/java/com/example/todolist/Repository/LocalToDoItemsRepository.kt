package com.example.todolist.Repository

import com.example.todolist.Model.ToDoItem

typealias ToDoListListener = (toDoList: List<ToDoItem>) -> Unit

class LocalToDoItemsRepository : IToDoItemsRepository {

    private val toDoItems: ArrayList<ToDoItem> = items
    private val listeners = mutableSetOf<ToDoListListener>()

    init {
        notifyListeners()
    }

    override fun addNewItem(item: ToDoItem) {
        val id = "tmpId"

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

    override fun changeItem(task: ToDoItem){
        val index = getIndexById(task.getId())
        toDoItems[index] = task
        notifyListeners()
    }

    override fun deleteItem(id: String) {
        val index = getIndexById(id)
        toDoItems.removeAt(index)
        notifyListeners()
    }

    override fun createNewItem() : ToDoItem {
        TODO("Not yet implemented")
    }

    private fun getIndexById(id: String): Int{
        return toDoItems.indexOfFirst { it.getId() == id }
    }

    companion object{
        private val items = arrayListOf<ToDoItem>(
            ToDoItem("1e", "Купить что-то", ToDoItem.Importance.DEFAULT, 1, false, 0, 0, false),
            ToDoItem("2r", "Купить что-то", ToDoItem.Importance.DEFAULT, 1, true, 0, 0, false),
            ToDoItem("3t", "Купить что-то, где-то, зачем-то, но зачем не очень понятно", ToDoItem.Importance.DEFAULT, 1, false, 0, 0, false),
            ToDoItem("6y", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрррррррррррррррррррррррррр", ToDoItem.Importance.DEFAULT, 1, true, 0, 0, false),
//            ToDoItem("1", "task1", ToDoItem.Importance.DEFAULT, 1, false, 0, 0),
//            ToDoItem("2", "task2", ToDoItem.Importance.DEFAULT, 1, true, 0, 0),
//            ToDoItem("3", "task3", ToDoItem.Importance.DEFAULT, 1, false, 0, 0),
//            ToDoItem("4", "task4", ToDoItem.Importance.DEFAULT, 1, true, 0, 0),
//            ToDoItem("5", "task5", ToDoItem.Importance.DEFAULT, 1, false, 0, 0),
//            ToDoItem("6", "task6", ToDoItem.Importance.DEFAULT, 1, true, 0, 0),
//            ToDoItem("1", "task1", ToDoItem.Importance.DEFAULT, 1, false, 0, 0),
//            ToDoItem("2", "task2", ToDoItem.Importance.DEFAULT, 1, true, 0, 0),
//            ToDoItem("3", "task3", ToDoItem.Importance.DEFAULT, 1, false, 0, 0),
//            ToDoItem("4", "task4", ToDoItem.Importance.DEFAULT, 1, true, 0, 0),
//            ToDoItem("5", "task5", ToDoItem.Importance.DEFAULT, 1, false, 0, 0),
//            ToDoItem("6", "task6", ToDoItem.Importance.DEFAULT, 1, true, 0, 0),
        )
    }
}