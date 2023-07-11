package com.example.todolist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.ui.Screens.ImportanceTextItem
import com.example.todolist.ui.Screens.onImportanceTextItemClick

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetContent(
    bottomSheetState: ModalBottomSheetState,
    updateImportanceAction: (importance: ToDoItem.Importance) -> Unit
){
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer
            )
            .padding(top = 10.dp, bottom = 10.dp)
    ) {
        ImportanceTextItem(
            text = stringResource(id = R.string.No),
            onClick = { onImportanceTextItemClick(
                importance = ToDoItem.Importance.DEFAULT,
                scope = scope,
                bottomSheetState = bottomSheetState,
                updateImportanceAction = updateImportanceAction
            ) }
        )
        ImportanceTextItem(
            text = stringResource(id = R.string.Low),
            onClick = { onImportanceTextItemClick(
                importance = ToDoItem.Importance.LOW,
                scope = scope,
                bottomSheetState = bottomSheetState,
                updateImportanceAction = updateImportanceAction
            ) }
        )
        ImportanceTextItem(
            text = stringResource(id = R.string.High),
            color = MaterialTheme.colorScheme.error,
            onClick = { onImportanceTextItemClick(
                importance = ToDoItem.Importance.HIGH,
                scope = scope,
                bottomSheetState = bottomSheetState,
                updateImportanceAction = updateImportanceAction
            ) }
        )
    }
}