package com.example.todolist.ui.screens.EditTaskScreen.components

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todolist.R
import com.example.todolist.ui.theme.body
import com.example.todolist.ui.theme.subhead
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun ImportanceSection(
    bottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded
    ),
    importanceText: String = "Нет",
    hideKeyboardAction: () -> Unit = {},
    highlightedState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
){
    val scope = rememberCoroutineScope()

    var errorColor = MaterialTheme.colorScheme.errorContainer
    var color: Color by remember { mutableStateOf(errorColor) }
    LaunchedEffect(highlightedState.value){
        if(highlightedState.value){
            Log.w("AAA", "animate")
            animate(
                0f,
                2f,
                animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
            ){ value, _ ->
                color = if(value <= 1) color.copy(alpha = value)
                else color.copy(alpha = 2 - value)
            }
        }
        else{
            color = color.copy(alpha = 0f)
        }
        highlightedState.value = false
    }
    Column {
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
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .background(color = color)
        )
    }
}