package com.example.todolist.ui.screens.EditTaskScreen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.ui.theme.body

@Composable
@Preview
fun EditText(
    text: String = "",
    updateTextAction: (text: String) -> Unit = {}
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