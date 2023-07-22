package com.example.todolist.ui.Screens.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DeadlineTimePicker(
    clockState: UseCaseState = rememberUseCaseState(visible = true),
    clockSelection: ClockSelection = ClockSelection.HoursMinutes{_, _ ->}
){
    ClockDialog(
        state = clockState,
        selection = clockSelection,
        config = ClockConfig(
            defaultTime = LocalTime.now(),
            is24HourFormat = true
        )
    )
}