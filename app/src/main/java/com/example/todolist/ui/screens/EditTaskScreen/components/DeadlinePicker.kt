package com.example.todolist.ui.screens.EditTaskScreen.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DeadlinePicker(
    calendarState: UseCaseState = rememberUseCaseState(visible = true),
    calendarSelection: CalendarSelection = CalendarSelection.Date{}
){
    CalendarDialog(
        state = calendarState,
        selection = calendarSelection,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true
        )
    )
}