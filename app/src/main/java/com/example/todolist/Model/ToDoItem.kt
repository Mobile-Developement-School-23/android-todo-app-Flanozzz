package com.example.todolist.Model

import android.os.Parcelable

data class ToDoItem(
    private val id: String,
    private var taskText: String,
    private var importance: Importance,
    private var deadline: Long,
    private var isDone: Boolean,
    private val dateOfCreate: Long,
    private var dateOfChange: Long,
    private var hasDeadline: Boolean
) {
    enum class Importance{
        LOW,
        DEFAULT,
        HIGH
    }

    fun getId(): String {
        return id
    }

    fun getTaskText(): String {
        return taskText
    }

    fun getImportance(): Importance {
        return importance
    }

    fun getDeadline(): Long {
        return deadline
    }

    fun isDone(): Boolean {
        return isDone
    }

    fun getDateOfCreate(): Long {
        return dateOfCreate
    }

    fun getDateOfChange(): Long {
        return dateOfChange
    }

    fun hasDeadline(): Boolean{
        return hasDeadline
    }



    fun setTaskText(newTaskText: String) {
        taskText = newTaskText
        dateOfChange = System.currentTimeMillis()
    }

    fun setImportance(newImportance: Importance) {
        importance = newImportance
        dateOfChange = System.currentTimeMillis()
    }

    fun setDeadline(newDeadline: Long) {
        deadline = newDeadline
        dateOfChange = System.currentTimeMillis()
        hasDeadline = true
    }

    fun setIsDone(newIsDone: Boolean) {
        isDone = newIsDone
        dateOfChange = System.currentTimeMillis()
    }

    fun removeDeadline(){
        hasDeadline = false
    }
}