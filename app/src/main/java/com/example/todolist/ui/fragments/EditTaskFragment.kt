package com.example.todolist.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import android.view.View.OnClickListener
import android.widget.CompoundButton
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.R
import com.example.todolist.ToDoApp
import com.example.todolist.ui.viewModels.SelectedTaskViewModel
import com.example.todolist.databinding.FragmentEditTaskBinding
import com.example.todolist.ui.viewModels.ToDoListViewModel
import com.example.todolist.ui.viewModels.ViewModelFactory
import com.example.todolist.utils.Constants.Companion.HIGH_IMPORTANCE_POPUP_MENU_BUTTON_ID
import com.example.todolist.utils.Constants.Companion.LOW_IMPORTANCE_POPUP_MENU_BUTTON_ID
import com.example.todolist.utils.Constants.Companion.NO_IMPORTANCE_POPUP_MENU_BUTTON_ID
import com.example.todolist.utils.getAndroidAttrTextColor
import com.example.todolist.utils.getFormattedDate
import com.example.todolist.utils.getUnixTime
import com.example.todolist.utils.hideKeyboard
import com.example.todolist.utils.setCurrentDateToDatePicker
import com.example.todolist.utils.setTextViewByImportance
import kotlinx.coroutines.launch
import javax.inject.Inject


class EditTaskFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentEditTaskBinding
    private val selectedTaskViewModel: SelectedTaskViewModel by activityViewModels{viewModelFactory}
    private val toDoListViewModel: ToDoListViewModel by activityViewModels { viewModelFactory }

    private var toDoItem: ToDoItem? = null
    private var importance: ToDoItem.Importance = ToDoItem.Importance.DEFAULT
    private var deadline: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false)

        setupDependencies()
        setupObservers()
        setupListeners()

        return binding.root
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
                importance = task.importance
                deadline = task.deadline
                setTaskView(task, selectedTaskViewModel.isNewTask())
            }
        }
    }

    private fun setupListeners() {
        binding.cancelButton.setOnClickListener(canselButtonListener)
        binding.importanceTextView.setOnClickListener(importanceButtonListener)
        binding.dateSwitcher.setOnCheckedChangeListener(dateSwitcherListener)
        binding.pickDateButton.setOnClickListener(pickDateButtonListener)
        binding.cancelPickDateButton.setOnClickListener(cancelPickDateButtonListener)
        binding.saveButton.setOnClickListener(saveButtonListener)
        binding.deleteButton.setOnClickListener(deleteButtonListener)
    }

    override fun onPause() {
        super.onPause()
        val newText = binding.editTextField.text.toString()
        selectedTaskViewModel.setNewData(importance, deadline, newText)
    }

    private val canselButtonListener = OnClickListener{
        goBackToList()
    }

    private val importanceButtonListener = OnClickListener {
        showImportancePopupMenu(it)
    }

    private val saveButtonListener = OnClickListener {
        val newText = binding.editTextField.text.toString()
        selectedTaskViewModel.setNewData(importance, deadline, newText)
        if(toDoItem != null){
            toDoListViewModel.saveToDoItem(toDoItem!!, selectedTaskViewModel.isNewTask())
        }
        goBackToList()
    }

    private val deleteButtonListener = OnClickListener {
        if(toDoItem != null){
            toDoListViewModel.deleteTask(toDoItem!!)
        }
        goBackToList()
    }

    private val pickDateButtonListener = OnClickListener {
        with(binding){
            deadline = getUnixTime(datePicker.dayOfMonth, datePicker.month, datePicker.year)
        }
        setDeadlineTextView(deadline)
        setDatePickerVisibility(View.GONE)
    }

    private val cancelPickDateButtonListener = OnClickListener {
        setDatePickerVisibility(View.GONE)
        deadline = null
        setSwitcherView(deadline)
    }

    private val dateSwitcherListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        hideKeyboard(requireActivity())
        if (isChecked) {
            setDatePickerVisibility(View.VISIBLE)
            setCurrentDateToDatePicker(binding.datePicker)
        } else {
            binding.deadlineDateTextView.visibility = View.INVISIBLE
            deadline = null
        }
    }

    private fun setDatePickerVisibility(visibility: Int){
        binding.disablePanel.visibility = visibility
        binding.datePickerContainer.visibility = visibility
    }

    private fun goBackToList(){
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun showImportancePopupMenu(view: View){
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menu.add(Menu.NONE, NO_IMPORTANCE_POPUP_MENU_BUTTON_ID, Menu.NONE, view.context.getString(R.string.No))
        popupMenu.menu.add(Menu.NONE, LOW_IMPORTANCE_POPUP_MENU_BUTTON_ID, Menu.NONE, view.context.getString(R.string.Low))
        popupMenu.menu.add(Menu.NONE, HIGH_IMPORTANCE_POPUP_MENU_BUTTON_ID, Menu.NONE, view.context.getString(R.string.Importance))

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                NO_IMPORTANCE_POPUP_MENU_BUTTON_ID -> {
                    changeTaskImportance(ToDoItem.Importance.DEFAULT)
                    true
                }
                LOW_IMPORTANCE_POPUP_MENU_BUTTON_ID -> {
                    changeTaskImportance(ToDoItem.Importance.LOW)
                    true
                }
                HIGH_IMPORTANCE_POPUP_MENU_BUTTON_ID -> {
                    changeTaskImportance(ToDoItem.Importance.HIGH)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun setDeleteButtonView(isNewTask: Boolean){
        val color: Int = if (isNewTask) {
            binding.deleteButton.isEnabled = false
            getAndroidAttrTextColor(requireContext(), android.R.attr.colorButtonNormal)
        } else {
            binding.deleteButton.isEnabled = true
            requireContext().getColor(R.color.red)
        }
        binding.deleteButton.setTextColor(color)
        binding.deleteButton.iconTint = ColorStateList.valueOf(color)
    }

    private fun setSwitcherView(deadline: Long?){
        binding.dateSwitcher.setOnCheckedChangeListener(null)
        binding.dateSwitcher.isChecked = deadline != null
        binding.dateSwitcher.setOnCheckedChangeListener(dateSwitcherListener)
    }

    private fun setDeadlineTextView(deadline: Long?){
        if (deadline != null){
            binding.deadlineDateTextView.visibility = View.VISIBLE
            binding.deadlineDateTextView.text = getFormattedDate(deadline)
        } else {
            binding.deadlineDateTextView.visibility = View.INVISIBLE
        }
    }

    private fun setEditFieldView(task: ToDoItem){
        binding.editTextField.setText(task.text)
    }

    private fun setTaskView(task: ToDoItem, isNewTask: Boolean){
        setEditFieldView(task)
        setTextViewByImportance(binding.importanceStateTextView, task.importance, requireContext())
        setDeadlineTextView(task.deadline)
        setSwitcherView(task.deadline)
        setDeleteButtonView(isNewTask)
    }

    private fun changeTaskImportance(importance: ToDoItem.Importance){
        this.importance = importance
        setTextViewByImportance(binding.importanceStateTextView, importance, requireContext())
    }
}