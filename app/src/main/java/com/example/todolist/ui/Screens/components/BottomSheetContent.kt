package com.example.todolist.ui.Screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.ui.Screens.components.ImportanceTextItem
import com.example.todolist.ui.Screens.components.onImportanceTextItemClick

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun BottomSheetContent(
    bottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    ),
    updateImportanceAction: (importance: ToDoItem.Importance) -> Unit = {},
    highlightedState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
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
            )
                highlightedState.value = true
            }
        )
    }
}