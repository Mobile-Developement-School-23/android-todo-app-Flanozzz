package com.example.todolist.ui.Screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.ui.components.BottomSheetContent
import com.example.todolist.ui.theme.AppTheme
import com.example.todolist.ui.theme.body
import com.example.todolist.ui.theme.button
import com.example.todolist.ui.theme.disabledPanelAlpha
import com.example.todolist.ui.theme.largeTitle
import com.example.todolist.ui.theme.subhead
import com.example.todolist.utils.getCurrentUnixTime
import com.example.todolist.utils.getDeviceId
import com.example.todolist.utils.getFormattedDate
import com.example.todolist.utils.getStringByImportance
import com.example.todolist.utils.getUnixTime
import com.example.todolist.utils.hideKeyboard
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun Screen(
    selectedTask: State<ToDoItem> = mutableStateOf(ToDoItem.getDefaultTask("")),
    actions: EditTaskScreenActions = EditTaskScreenActions.getEmptyActions()
){
    AppTheme {
        val calendarState = rememberUseCaseState()
        val modalBottomSheetState = androidx.compose.material.rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden
        )

        ModalBottomSheetLayout(
            sheetState = modalBottomSheetState,
            sheetContent = { BottomSheetContent(
                bottomSheetState = modalBottomSheetState,
                updateImportanceAction = actions.updateImportanceAction
            )},
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            val scrollState = rememberScrollState()
            Box(modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
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
                            hideKeyboardAction = actions.hideKeyboardAction
                        )
                        Divider(
                            Modifier.padding(top = 16.dp, bottom = 16.dp)
                        )
                        DeadlineSection(
                            selectedTask = selectedTask.value,
                            calendarState = calendarState,
                            updateDeadlineAction = actions.updateDeadlineAction
                        )
                    }
                    Divider(
                        Modifier.padding(top = 16.dp, bottom = 16.dp)
                    )
                    DeleteTaskButton(actions.deleteButtonClickAction)
                    Row(modifier = Modifier.height(400.dp)) {}
                }
                DeadlinePicker(calendarState, actions.calendarSelection)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlinePicker(
    calendarState: UseCaseState,
    calendarSelection: CalendarSelection
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

@Composable
fun DeleteTaskButton(
    onClick: () -> Unit
){
    TextButton(
        modifier = Modifier.padding(start = 2.dp),
        onClick = { onClick() }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_baseline_delete_24),
            contentDescription = "delete"
        )
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = stringResource(id = R.string.Delete)
        )
    }
}

@Composable
fun DeadlineSection(
    selectedTask: ToDoItem,
    calendarState: UseCaseState,
    updateDeadlineAction: (isChecked: Boolean) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.make_to),
                style = body,
                color = MaterialTheme.colorScheme.onBackground
            )
            val date = selectedTask.deadline
            Text(
                modifier = Modifier
                    .clickable { calendarState.show() },
                text = if (date != null) getFormattedDate(date) else "",
                style = subhead,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        val isChecked = remember { mutableStateOf(false) }
        Switch(
            checked = selectedTask.deadline != null,
            onCheckedChange = {
                isChecked.value = !isChecked.value
                updateDeadlineAction(it)
            }
        )
    }
}

@Composable
fun DisabledPanel(){
    Box(modifier = Modifier
        .fillMaxSize()
        .alpha(disabledPanelAlpha)
        .background(color = MaterialTheme.colorScheme.outline)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImportanceSection(
    bottomSheetState: ModalBottomSheetState,
    importanceText: String,
    hideKeyboardAction: () -> Unit
){
    val scope = rememberCoroutineScope()
    Text(
        text = stringResource(id = R.string.Importance),
        Modifier
            .clickable {
                scope.launch {
                    if(bottomSheetState.currentValue != ModalBottomSheetValue.Expanded){
                        hideKeyboardAction()
                        bottomSheetState.show()
                    }
                    else{
                        bottomSheetState.hide()
                    }
                }
            },
        style = body,
        color = MaterialTheme.colorScheme.onBackground
    )
    Text(
        text = importanceText,
        style = subhead,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@OptIn(ExperimentalMaterialApi::class)
fun onImportanceTextItemClick(
    scope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState,
    importance: ToDoItem.Importance,
    updateImportanceAction: (importance: ToDoItem.Importance) -> Unit
){
    updateImportanceAction(importance)
    scope.launch {
        bottomSheetState.hide()
    }
}

@Composable
fun ImportanceTextItem(
    onClick: () -> Unit = {},
    text: String = stringResource(id = R.string.No),
    color: Color = MaterialTheme.colorScheme.onSecondaryContainer
){
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = largeTitle,
            color = color
        )
    }
}

@Composable
fun EditField(
    text: String,
    updateTextAction: (text: String) -> Unit
){
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        EditText(
            text = text,
            updateTextAction = updateTextAction
        )
    }
}

@Composable
fun Header(
    goBackAction: () -> Unit,
    saveButtonAction: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { goBackAction() }
        ) {
            Icon(
                imageVector = Icons.Sharp.Close,
                contentDescription = "close",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        TextButton(
            modifier = Modifier.padding(end = 4.dp),
            onClick = { saveButtonAction() }
        ) {
            Text(
                text = stringResource(id = R.string.save),
                style = button
            )
        }
    }
}

@Composable
fun EditText(
    text: String,
    updateTextAction: (text: String) -> Unit
){
    Card(
        modifier = Modifier.padding(bottom = 28.dp)
    ){
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 104.dp),
            value = text,
            onValueChange = { updateTextAction(it) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.what_to_do),
                    style = body
                )
            }
        )
    }
}
