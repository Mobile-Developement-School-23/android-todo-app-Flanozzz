package com.example.todolist.ui.screens.EditTaskScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.ui.theme.body
import com.example.todolist.ui.theme.subhead
import com.example.todolist.utils.unixTimeToDMY
import com.example.todolist.utils.unixTimeToHHMM
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState

@Composable
@Preview
fun DeadlineSection(
    selectedTask: ToDoItem = ToDoItem.getDefaultTask(""),
    calendarState: UseCaseState = rememberUseCaseState(),
    clockState: UseCaseState = rememberUseCaseState(),
    updateDeadlineAction: (isChecked: Boolean) -> Unit = {}
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.make_to),
                style = body,
                color = MaterialTheme.colorScheme.onBackground
            )
            val date = selectedTask.deadline
            Row {
                Text(
                    text = if (date != null) unixTimeToDMY(date) else "",
                    style = subhead,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.clickable { calendarState.show() }
                )
                Text(
                    text = if (date != null) unixTimeToHHMM(date) else "",
                    style = subhead,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .clickable { clockState.show() }
                )
            }
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