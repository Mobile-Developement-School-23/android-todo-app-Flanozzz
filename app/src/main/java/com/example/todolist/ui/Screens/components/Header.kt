package com.example.todolist.ui.Screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.ui.theme.button

@Composable
@Preview
fun Header(
    goBackAction: () -> Unit = {},
    saveButtonAction: () -> Unit = {}
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