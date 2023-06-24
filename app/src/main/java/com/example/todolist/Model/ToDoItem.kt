package com.example.todolist.Model

import android.os.Parcelable
import com.example.todolist.Utils.getCurrentUnixTime
import com.google.gson.annotations.SerializedName

data class ToDoItem(
    @SerializedName("id") private val id: String,
    @SerializedName("text") private var taskText: String,
    @SerializedName("importance") private var importance: Importance,
    @SerializedName("deadline") private var deadline: Long,
    @SerializedName("done") private var isDone: Boolean,
    @SerializedName("created_at") private val dateOfCreate: Long,
    @SerializedName("changed_at") private var dateOfChange: Long,
    private var hasDeadline: Boolean = false
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