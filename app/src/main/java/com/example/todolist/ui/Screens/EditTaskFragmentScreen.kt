package com.example.todolist.ui.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.ui.Screens.actions.EditTaskScreenActions
import com.example.todolist.ui.Screens.components.DeadlinePicker
import com.example.todolist.ui.Screens.components.DeadlineSection
import com.example.todolist.ui.Screens.components.DeadlineTimePicker
import com.example.todolist.ui.Screens.components.DeleteTaskButton
import com.example.todolist.ui.Screens.components.EditField
import com.example.todolist.ui.Screens.components.Header
import com.example.todolist.ui.Screens.components.ImportanceSection
import com.example.todolist.ui.Screens.components.BottomSheetContent
import com.example.todolist.ui.theme.AppTheme
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun Screen(
    selectedTask: State<ToDoItem> = mutableStateOf(ToDoItem.getDefaultTask("")),
    actions: EditTaskScreenActions = EditTaskScreenActions.getEmptyActions(),
    isNewTask: Boolean = false
){
    AppTheme {
        val calendarState = rememberUseCaseState()
        val clockState = rememberUseCaseState()
        val modalBottomSheetState = androidx.compose.material.rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden
        )
        val scrollState = rememberScrollState()
        val highlightedState = remember{mutableStateOf(false)}
        ModalBottomSheetLayout(
            sheetState = modalBottomSheetState,
            sheetContent = { BottomSheetContent(
                bottomSheetState = modalBottomSheetState,
                updateImportanceAction = actions.updateImportanceAction,
                highlightedState
            )
            },
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
            ){
                Column {
                    Header(
                        goBackAction = actions.goBackAction,
                        saveButtonAction = actions.saveButtonAction
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        EditField(
                            text = selectedTask.value.text,
                            updateTextAction = actions.updateTextAction
                        )
                        ImportanceSection(
                            bottomSheetState = modalBottomSheetState,
                            importanceText = actions.getStringByImportanceAction(
                                selectedTask.value.importance
                            ),
                            hideKeyboardAction = actions.hideKeyboardAction,
                            highlightedState = highlightedState
                        )
                        Divider(
                            Modifier.padding(top = 16.dp, bottom = 16.dp)
                        )
                        DeadlineSection(
                            selectedTask = selectedTask.value,
                            calendarState = calendarState,
                            clockState = clockState,
                            updateDeadlineAction = actions.updateDeadlineAction
                        )
                    }
                    Divider(
                        Modifier.padding(top = 16.dp, bottom = 16.dp)
                    )
                    DeleteTaskButton(actions.deleteButtonClickAction, isNewTask = isNewTask)
                }
            }

        }
        DeadlineTimePicker(clockState = clockState, clockSelection = actions.clockSelection)
        DeadlinePicker(calendarState, actions.calendarSelection)
    }
}

