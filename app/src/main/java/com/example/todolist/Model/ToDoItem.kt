package com.example.todolist.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "to_do_items_table")
data class ToDoItem(
    @PrimaryKey
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("importance") val importance: Importance,
    @SerializedName("deadline") val deadline: Long?,
    @SerializedName("done") val isDone: Boolean,
    @SerializedName("color") val color: String?,
    @SerializedName("created_at") val dateOfCreate: Long,
    @SerializedName("changed_at") val dateOfChange: Long
) {
    enum class Importance{
        LOW,
        DEFAULT,
        HIGH
    }
}