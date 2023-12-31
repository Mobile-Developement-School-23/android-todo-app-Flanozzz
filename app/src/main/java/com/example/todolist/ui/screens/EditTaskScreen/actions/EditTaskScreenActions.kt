package com.example.todolist.ui.screens.EditTaskScreen.actions

import com.example.todolist.data.model.ToDoItem
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.models.ClockSelection

data class EditTaskScreenActions(
    val calendarSelection: CalendarSelection,
    val clockSelection: ClockSelection,
    val deleteButtonClickAction: () -> Unit,
    val updateDeadlineAction: (isChecked: Boolean) -> Unit,
    val updateImportanceAction: (importance: ToDoItem.Importance) -> Unit,
    val goBackAction: () -> Unit,
    val saveButtonAction: () -> Unit,
    val updateTextAction: (text: String) -> Unit,
    val hideKeyboardAction: () -> Unit,
    val getStringByImportanceAction: (importance: ToDoItem.Importance) -> String,
    val shareAction: () -> Unit
){
    companion object{
        fun getEmptyActions(): EditTaskScreenActions {
            return EditTaskScreenActions(
                CalendarSelection.Date{},
                ClockSelection.HoursMinutes{_, _ -> },
                {}, {}, {}, {}, {}, {}, {},
                { return@EditTaskScreenActions "" },
                {}
            )
        }
    }
}