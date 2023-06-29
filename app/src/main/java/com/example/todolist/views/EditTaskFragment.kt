package com.example.todolist.views

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.todolist.models.ToDoItem
import com.example.todolist.R
import com.example.todolist.utils.*
import com.example.todolist.viewModels.SelectedTaskViewModel
import com.example.todolist.databinding.FragmentEditTaskBinding
import com.example.todolist.deviceIdFactory
import kotlinx.coroutines.launch
import java.util.*


class EditTaskFragment : Fragment() {

    private lateinit var binding: FragmentEditTaskBinding
    private val viewModel: SelectedTaskViewModel by activityViewModels{deviceIdFactory()}

    private lateinit var importance: ToDoItem.Importance
    private var deadline: Long? = null
    private var text = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            viewModel.selectedTaskFlow.collect{
                setTaskView(it, viewModel.isNewTask())
                importance = it.importance
                deadline = it.deadline
            }
        }

        binding.cancelButton.setOnClickListener(canselButtonListener)
        binding.importanceTextView.setOnClickListener(importanceButtonListener)
        binding.dateSwitcher.setOnCheckedChangeListener(dateSwitcherListener)
        binding.pickDateButton.setOnClickListener(pickDateButtonListener)
        binding.cancelPickDateButton.setOnClickListener(cancelPickDateButtonListener)
        binding.saveButton.setOnClickListener(saveButtonListener)
        binding.deleteButton.setOnClickListener(deleteButtonListener)
        binding.editTextField.addTextChangedListener(textWatcher)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearSelectedTask()
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            text = s.toString()
        }
    }

    private val canselButtonListener = OnClickListener{
        goBackToList()
    }

    private val importanceButtonListener = OnClickListener {
        showImportancePopupMenu(it)
    }

    private val saveButtonListener = OnClickListener {
        viewModel.saveTask(importance, deadline, binding.editTextField.text.toString())
        goBackToList()
    }

    private val deleteButtonListener = OnClickListener {
        viewModel.deleteTask()
        goBackToList()
    }

    private val pickDateButtonListener = OnClickListener {
        with(binding){
            deadline = getUnixTime(datePicker.dayOfMonth, datePicker.month, datePicker.year)
        }
        setDeadlineTextView(deadline)
        binding.datePickerContainer.visibility = View.GONE
        binding.disablePanel.visibility = View.GONE
    }

    private val cancelPickDateButtonListener = OnClickListener {
        binding.disablePanel.visibility = View.GONE
        binding.datePickerContainer.visibility = View.GONE
    }

    private val dateSwitcherListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        val view: View? = requireActivity().currentFocus
        if(view != null){
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        if (isChecked) {
            binding.disablePanel.visibility = View.VISIBLE
            binding.datePickerContainer.visibility = View.VISIBLE
            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            binding.datePicker.updateDate(year, month, day)

        }
        else {
            binding.deadlineDateTextView.visibility = View.INVISIBLE
            viewModel.removeTaskDeadline()
        }
    }

    private fun goBackToList(){
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun showImportancePopupMenu(view: View){
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menu.add(Menu.NONE, NO, Menu.NONE, view.context.getString(R.string.No))
        popupMenu.menu.add(Menu.NONE, LOW, Menu.NONE, view.context.getString(R.string.Low))
        popupMenu.menu.add(Menu.NONE, HIGH, Menu.NONE, view.context.getString(R.string.Importance))

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                NO -> {
                    changeTaskImportance(ToDoItem.Importance.DEFAULT)
                    true
                }
                LOW -> {
                    changeTaskImportance(ToDoItem.Importance.LOW)
                    true
                }
                HIGH -> {
                    changeTaskImportance(ToDoItem.Importance.HIGH)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun setDeleteButtonView(isNewTask: Boolean){
        val color: Int
        if(isNewTask){
            binding.deleteButton.isEnabled = false
            color = getAndroidAttrTextColor(requireContext(), android.R.attr.colorButtonNormal)

        }
        else{
            binding.deleteButton.isEnabled = true
            color = requireContext().getColor(R.color.red)
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
        }
        else{
            binding.deadlineDateTextView.visibility = View.INVISIBLE
        }
    }

    private fun setEditFieldView(task: ToDoItem){
        binding.editTextField.setText(task.text)
    }

    private fun setImportanceView(importance: ToDoItem.Importance){
        when(importance){
            ToDoItem.Importance.DEFAULT -> binding.importanceStateTextView.text = getString(R.string.No)
            ToDoItem.Importance.LOW -> binding.importanceStateTextView.text = getString(R.string.Low)
            ToDoItem.Importance.HIGH -> binding.importanceStateTextView.text = getString(R.string.High)
        }
    }

    private fun setTaskView(task: ToDoItem, isNewTask: Boolean){
        setEditFieldView(task)
        setImportanceView(task.importance)
        setDeadlineTextView(task.deadline)
        setSwitcherView(task.deadline)
        setDeleteButtonView(isNewTask)
    }

    private fun changeTaskImportance(importance: ToDoItem.Importance){
        this.importance = importance
        setImportanceView(importance)
    }

    companion object{
        const val NO = 0
        const val LOW = 1
        const val HIGH = 2
    }
}