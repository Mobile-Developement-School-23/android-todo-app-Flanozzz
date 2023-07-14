package com.example.todolist.data.api

import com.example.todolist.data.model.ToDoItem
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class ImportanceConverter : JsonSerializer<ToDoItem.Importance>,
    JsonDeserializer<ToDoItem.Importance> {

    override fun serialize(
        src: ToDoItem.Importance?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val importanceString = when (src) {
            ToDoItem.Importance.LOW -> "low"
            ToDoItem.Importance.DEFAULT -> "basic"
            ToDoItem.Importance.HIGH -> "important"
            else -> throw IllegalArgumentException("Unknown importance value: $src")
        }
        return JsonPrimitive(importanceString)
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ToDoItem.Importance {
        return when (val importanceString = json?.asString) {
            "low" -> ToDoItem.Importance.LOW
            "basic" -> ToDoItem.Importance.DEFAULT
            "important" -> ToDoItem.Importance.HIGH
            else -> throw JsonParseException("Unknown importance value: $importanceString")
        }
    }
}