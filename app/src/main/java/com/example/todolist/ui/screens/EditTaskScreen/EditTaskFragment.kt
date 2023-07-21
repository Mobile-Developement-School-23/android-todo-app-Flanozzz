package com.example.todolist.ui.screens.EditTaskScreen

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.todolist.R
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.ToDoApp
import com.example.todolist.databinding.FragmentEditTaskBinding
import com.example.todolist.ui.screens.EditTaskScreen.actions.EditTaskScreenActions
import com.example.todolist.ui.viewModels.SelectedTaskViewModel
import com.example.todolist.ui.theme.AppTheme
import com.example.todolist.ui.viewModels.ToDoListViewModel
import com.example.todolist.ui.viewModels.ViewModelFactory
import com.example.todolist.utils.getCurrentUnixTime
import com.example.todolist.utils.getDeviceId
import com.example.todolist.utils.getEndOfDayUnixTime
import com.example.todolist.utils.getNextDayUnixTime
import com.example.todolist.utils.getStringByImportance
import com.example.todolist.utils.getUnixTime
import com.example.todolist.utils.hideKeyboard
import com.example.todolist.utils.setUnixTime
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.models.ClockSelection
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditTaskFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val selectedTaskViewModel: SelectedTaskViewModel by activityViewModels{viewModelFactory}
    private val toDoListViewModel: ToDoListViewModel by activityViewModels { viewModelFactory }
    private lateinit var binding: FragmentEditTaskBinding

    private var toDoItem: ToDoItem? = null
    private var isNewTask: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditTaskBinding.inflate(inflater, container, false)

        setupDependencies()
        setupObservers()
        Log.d("lifecycle", "edit task fragment - onCreateView")

        binding.composeView.apply {
            findViewById<ComposeView>(R.id.composeView).setContent {
                AppTheme {
                    val selectedTask = selectedTaskViewModel
                        .selectedTaskFlow
                        .collectAsState(initial = ToDoItem.getDefaultTask(getDeviceId(requireContext())))

                    val isNewTaskState = selectedTaskViewModel
                        .isNewTaskFlow
                        .collectAsState(initial = isNewTask)

                    Log.w("AAA", isNewTaskState.value.toString())

                    Screen(
                        selectedTask = selectedTask,
                        actions = EditTaskScreenActions(
                            calendarSelection = calendarSelection,
                            clockSelection = clockSelection,
                            deleteButtonClickAction = deleteTackAction,
                            updateDeadlineAction = updateDeadlineAction,
                            updateImportanceAction = updateImportanceAction,
                            goBackAction = { goBackToList() },
                            saveButtonAction = saveTaskAction,
                            updateTextAction = updateTextAction,
                            hideKeyboardAction = { hideKeyboard(requireActivity()) },
                            getStringByImportanceAction = { getStringByImportance(it, requireContext()) }
                        ),
                        isNewTask = isNewTaskState.value
                    )
                }
            }
        }

        return binding.root
    }

    private val calendarSelection = CalendarSelection.Date {
        val newDeadline = getEndOfDayUnixTime(
            getUnixTime(it.dayOfMonth, it.monthValue - 1, it.year)
        )
        if (checkTimeRelevance(newDeadline)){
            selectedTaskViewModel.updateDeadline(newDeadline)
        }
    }

    private val clockSelection = ClockSelection.HoursMinutes{ hours, minutes ->
        if(toDoItem != null && toDoItem!!.deadline != null){
            val newDeadline = setUnixTime(hours, minutes, toDoItem!!.deadline!!)
            if (checkTimeRelevance(newDeadline)){
                selectedTaskViewModel.updateDeadline(newDeadline)
            }
        }
    }

    private val deleteTackAction = {
        if(toDoItem != null) toDoListViewModel.deleteTask(toDoItem!!)
        goBackToList()
    }

    private val updateDeadlineAction: (isChecked: Boolean) -> Unit = { isChecked ->
        val newDeadline = if(isChecked) getNextDayUnixTime() else null
        selectedTaskViewModel.updateDeadline(newDeadline)
    }

    private val updateImportanceAction: (importance: ToDoItem.Importance) -> Unit = {
        selectedTaskViewModel.updateImportance(it)
    }

    private val saveTaskAction = {
        if (toDoItem != null){
            toDoListViewModel.saveToDoItem(
                toDoItem!!,
                isNewTask
            )
        }
        goBackToList()
    }

    private val updateTextAction: (text: String) -> Unit = {
        selectedTaskViewModel.updateText(it)
    }

    private fun checkTimeRelevance(time: Long): Boolean{
        if(time <= getCurrentUnixTime()){
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.time_has_already_passed),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
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
        lifecycleScope.launch {
            selectedTaskViewModel.isNewTaskFlow.collect{
                isNewTask = it
            }
        }
    }

    private fun goBackToList(){
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

}