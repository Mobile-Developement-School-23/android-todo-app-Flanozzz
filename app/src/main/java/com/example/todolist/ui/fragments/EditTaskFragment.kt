package com.example.todolist.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.ToDoApp
import com.example.todolist.ui.Screens.EditTaskScreenActions
import com.example.todolist.ui.Screens.Screen
import com.example.todolist.ui.viewModels.SelectedTaskViewModel
import com.example.todolist.ui.theme.AppTheme
import com.example.todolist.ui.viewModels.ToDoListViewModel
import com.example.todolist.ui.viewModels.ViewModelFactory
import com.example.todolist.utils.getCurrentUnixTime
import com.example.todolist.utils.getDeviceId
import com.example.todolist.utils.getNextDayBeginUnixTime
import com.example.todolist.utils.getStringByImportance
import com.example.todolist.utils.getUnixTime
import com.example.todolist.utils.hideKeyboard
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class EditTaskFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val selectedTaskViewModel: SelectedTaskViewModel by activityViewModels{viewModelFactory}
    private val toDoListViewModel: ToDoListViewModel by activityViewModels { viewModelFactory }

    private var toDoItem: ToDoItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setupDependencies()
        setupObservers()
        Log.d("lifecycle", "edit task fragment - onCreateView")
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    val selectedTask = selectedTaskViewModel
                        .selectedTaskFlow
                        .collectAsState(initial = ToDoItem.getDefaultTask(getDeviceId(requireContext())))

                    Screen(
                        selectedTask = selectedTask,
                        actions = getActions()
                    )
                }
            }
        }
    }

    private fun getActions(): EditTaskScreenActions{
        return EditTaskScreenActions(
            calendarSelection = CalendarSelection.Date {
                selectedTaskViewModel.updateDeadline(
                    getUnixTime(it.dayOfMonth, it.monthValue - 1, it.year)
                )
            },
            deleteButtonClickAction = {
                if(toDoItem != null) toDoListViewModel.deleteTask(toDoItem!!)
                goBackToList()
            },
            updateDeadlineAction = { isChecked ->
                val newDeadline = if(isChecked) getCurrentUnixTime() else null
                selectedTaskViewModel.updateDeadline(newDeadline)
            },
            updateImportanceAction = {
                selectedTaskViewModel.updateImportance(it)
            },
            goBackAction = { goBackToList() },
            saveButtonAction = {
                if (toDoItem != null){
                    toDoListViewModel.saveToDoItem(toDoItem!!, selectedTaskViewModel.isNewTask())
                }
                goBackToList()
            },
            updateTextAction = {
                selectedTaskViewModel.updateText(it)
            },
            hideKeyboardAction = { hideKeyboard(requireActivity()) },
            getStringByImportanceAction = { getStringByImportance(it, requireContext()) }
        )
    }

    private fun setupDependencies(){
        (requireContext().applicationContext as ToDoApp)
            .appComponent
            .activityComponent()
            .editTaskFragmentComponent()
            .inject(this)
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            selectedTaskViewModel.selectedTaskFlow.collect { task ->
                toDoItem = task
            }
        }
    }

    private fun goBackToList(){
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

}