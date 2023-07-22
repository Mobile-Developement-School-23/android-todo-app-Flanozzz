package com.example.todolist.ui.Screens.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.R

@Composable
@Preview
fun DeleteTaskButton(
    onClick: () -> Unit = {},
    isNewTask: Boolean = false
){
    TextButton(
        modifier = Modifier.padding(start = 2.dp),
        onClick = { onClick() },
        enabled = !isNewTask
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