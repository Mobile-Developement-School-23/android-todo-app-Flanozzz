package com.example.todolist.View

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.todolist.Model.ToDoItem
import com.example.todolist.R
import com.example.todolist.Utils.*
import com.example.todolist.ViewModel.SelectedTaskViewModel
import com.example.todolist.databinding.FragmentEditTaskBinding
import com.example.todolist.myFactory
import java.util.*


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
        showImportancePopupMenu(it)
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
        binding.disablePanel.visibility = View.GONE
    }

    private val cancelPickDateButtonListener = OnClickListener {
        binding.disablePanel.visibility = View.GONE
        binding.datePickerContainer.visibility = View.GONE
        setSwitcherView(viewModel.selectedTaskLiveData.value!!)
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
        val item1 = popupMenu.menu.add(Menu.NONE, NO, Menu.NONE, view.context.getString(R.string.No))
        val item2 = popupMenu.menu.add(Menu.NONE, LOW, Menu.NONE, view.context.getString(R.string.Low))
        val item3 = popupMenu.menu.add(Menu.NONE, HIGH, Menu.NONE, view.context.getString(R.string.Importance))

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

    private fun changeTaskImportance(importance: ToDoItem.Importance){
        viewModel.setTaskImportance(importance)
        setImportanceView(viewModel.selectedTaskLiveData.value!!)
    }

    companion object{
        const val NO = 0
        const val LOW = 1
        const val HIGH = 2
    }
}