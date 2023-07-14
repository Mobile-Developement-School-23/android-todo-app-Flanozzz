package com.example.todolist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.utils.getCurrentUnixTime
import com.google.gson.annotations.SerializedName
import java.util.UUID

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
    @SerializedName("changed_at") val dateOfChange: Long,
    @SerializedName("last_updated_by") val lastUpdatedBy: String
) {
    enum class Importance{
        LOW,
        DEFAULT,
        HIGH
    }

    companion object{
        fun getDefaultTask(deviceId: String): ToDoItem {
            return ToDoItem(
                id = UUID.randomUUID().toString(),
                text = "",
                importance = Importance.DEFAULT,
                deadline = null,
                isDone = false,
                dateOfCreate = getCurrentUnixTime(),
                dateOfChange = getCurrentUnixTime(),
                color = null,
                lastUpdatedBy = deviceId
            )
        }
    }
}