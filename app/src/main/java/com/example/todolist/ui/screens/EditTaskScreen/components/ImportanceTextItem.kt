package com.example.todolist.ui.screens.EditTaskScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.ui.theme.largeTitle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
fun onImportanceTextItemClick(
    scope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState,
    importance: ToDoItem.Importance,
    updateImportanceAction: (importance: ToDoItem.Importance) -> Unit,

    ){

    updateImportanceAction(importance)
    scope.launch {
        bottomSheetState.hide()
    }
}

@Composable
@Preview
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