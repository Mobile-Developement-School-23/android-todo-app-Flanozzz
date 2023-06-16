package com.example.todolist.View

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.todolist.Model.ToDoItem
import com.example.todolist.R
import com.example.todolist.Utils.*
import com.example.todolist.ViewModel.SelectedTaskViewModel
import com.example.todolist.databinding.FragmentEditTaskBinding
import com.example.todolist.myFactory
import kotlin.properties.Delegates

class EditTaskFragment : Fragment() {

    private lateinit var binding: FragmentEditTaskBinding
    private val viewModel: SelectedTaskViewModel by activityViewModels { myFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false)

        setTaskView(viewModel.selectedTaskLiveData.value!!, viewModel.isNewTaskLiveData.value!!)

        binding.cancelButton.setOnClickListener(canselButtonListener)
        binding.importanceTextView.setOnClickListener(importanceButtonListener)
        binding.dateSwitcher.setOnCheckedChangeListener(dateSwitcherListener)
        binding.pickDateButton.setOnClickListener(pickDateButtonListener)
        binding.cancelPickDateButton.setOnClickListener(cancelPickDateButtonListener)
        binding.editTextField.addTextChangedListener(editTextFieldListener)
        binding.saveButton.setOnClickListener(saveButtonListener)
        binding.deleteButton.setOnClickListener(deleteButtonListener)

        return binding.root
    }

    private val canselButtonListener = OnClickListener{
        goBackToList()
    }

    private val importanceButtonListener = OnClickListener {
        val popupWindow = PopupMenuCreator.showPopupMenu(it, R.layout.importance_pop_up_menu)
        setListenersOnPopupMenuItems(popupWindow.contentView, popupWindow)
    }

    private val saveButtonListener = OnClickListener {
        viewModel.saveTask()
        goBackToList()
    }

    private val deleteButtonListener = OnClickListener {
        viewModel.deleteTask()
        goBackToList()
    }

    private val editTextFieldListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.setTaskText(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }

    private val pickDateButtonListener = OnClickListener {
        with(binding){
            viewModel.setTaskDeadline(getUnixTime(datePicker.dayOfMonth, datePicker.month, datePicker.year))
        }
        setDeadlineTextView(viewModel.selectedTaskLiveData.value!!)
        binding.datePickerContainer.visibility = View.GONE
    }

    private val cancelPickDateButtonListener = OnClickListener {
        binding.datePickerContainer.visibility = View.GONE
        setSwitcherView(viewModel.selectedTaskLiveData.value!!)
    }

    private val dateSwitcherListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            binding.datePickerContainer.visibility = View.VISIBLE
        }
        else {
            binding.deadlineDateTextView.visibility = View.INVISIBLE
            viewModel.removeTaskDeadline()
        }
    }

    private fun goBackToList(){
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun setDeleteButtonView(isNewTask: Boolean){
        if(isNewTask){
            binding.deleteButton.isEnabled = false
            val disableColor = getAndroidAttrTextColor(requireContext(), android.R.attr.colorButtonNormal)
            binding.deleteButton.setTextColor(disableColor)
            binding.deleteButton.iconTint = ColorStateList.valueOf(disableColor)

        }
    }

    private fun setSwitcherView(task: ToDoItem){
        binding.dateSwitcher.setOnCheckedChangeListener(null)
        binding.dateSwitcher.isChecked = task.hasDeadline()
        binding.dateSwitcher.setOnCheckedChangeListener(dateSwitcherListener)
    }

    private fun setDeadlineTextView(task: ToDoItem){
        if (task.hasDeadline()){
            binding.deadlineDateTextView.visibility = View.VISIBLE
            binding.deadlineDateTextView.text = getFormattedDate(task.getDeadline())
        }
        else{
            binding.deadlineDateTextView.visibility = View.INVISIBLE
        }
    }

    private fun setEditFieldView(task: ToDoItem){
        binding.editTextField.setText(task.getTaskText())
    }

    private fun setImportanceView(task: ToDoItem){
        when(task.getImportance()){
            ToDoItem.Importance.DEFAULT -> binding.importanceStateTextView.text = getString(R.string.No)
            ToDoItem.Importance.LOW -> binding.importanceStateTextView.text = getString(R.string.Low)
            ToDoItem.Importance.HIGH -> binding.importanceStateTextView.text = getString(R.string.High)
        }
    }

    private fun setTaskView(task: ToDoItem, isNewTask: Boolean){
        setEditFieldView(task)
        setImportanceView(task)
        setDeadlineTextView(task)
        setSwitcherView(task)
        setDeleteButtonView(isNewTask)
    }

    private fun setListenersOnPopupMenuItems(container: View, popupWindow: PopupWindow){
        container.findViewById<TextView>(R.id.noTextView).setOnClickListener{
            changeTaskImportance(ToDoItem.Importance.DEFAULT)
            popupWindow.dismiss()
        }
        container.findViewById<TextView>(R.id.lowTextView).setOnClickListener{
            changeTaskImportance(ToDoItem.Importance.LOW)
            popupWindow.dismiss()
        }
        container.findViewById<TextView>(R.id.highTextView).setOnClickListener{
            changeTaskImportance(ToDoItem.Importance.HIGH)
            popupWindow.dismiss()
        }
    }

    private fun changeTaskImportance(importance: ToDoItem.Importance){
        viewModel.setTaskImportance(importance)
        setImportanceView(viewModel.selectedTaskLiveData.value!!)
    }
}