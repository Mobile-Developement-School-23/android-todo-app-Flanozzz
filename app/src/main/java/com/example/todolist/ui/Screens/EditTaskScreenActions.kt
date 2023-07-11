package com.example.todolist.ui.Screens

import com.example.todolist.data.model.ToDoItem
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

data class EditTaskScreenActions(
    val calendarSelection: CalendarSelection,
    val deleteButtonClickAction: () -> Unit,
    val updateDeadlineAction: (isChecked: Boolean) -> Unit,
    val updateImportanceAction: (importance: ToDoItem.Importance) -> Unit,
    val goBackAction: () -> Unit,
    val saveButtonAction: () -> Unit,
    val updateTextAction: (text: String) -> Unit,
    val hideKeyboardAction: () -> Unit,
    val getStringByImportanceAction: (importance: ToDoItem.Importance) -> String
){
    companion object{
        fun getEmptyActions(): EditTaskScreenActions{
            return EditTaskScreenActions(
                CalendarSelection.Date{},
                {}, {}, {}, {}, {}, {}, {},
                { return@EditTaskScreenActions "" },
            )
        }
    }
}