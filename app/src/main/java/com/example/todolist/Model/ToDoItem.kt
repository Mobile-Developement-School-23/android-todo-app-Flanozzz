package com.example.todolist.Model

import android.os.Parcelable
import com.example.todolist.Utils.getCurrentUnixTime

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
        dateOfChange = getCurrentUnixTime()
    }

    fun setImportance(newImportance: Importance) {
        importance = newImportance
        dateOfChange = getCurrentUnixTime()
    }

    fun setDeadline(newDeadline: Long) {
        deadline = newDeadline
        dateOfChange = getCurrentUnixTime()
        hasDeadline = true
    }

    fun setIsDone(newIsDone: Boolean) {
        isDone = newIsDone
        dateOfChange = getCurrentUnixTime()
    }

    fun removeDeadline(){
        hasDeadline = false
    }
}