package com.example.todolist

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.todolist.Utils.getFormattedDate
import com.example.todolist.Utils.getUnixTime
import com.example.todolist.ViewModel.SelectedTaskViewModel
import com.example.todolist.databinding.FragmentEditTaskBinding
import kotlin.properties.Delegates

class EditTaskFragment : Fragment() {

    private lateinit var binding: FragmentEditTaskBinding
    private val viewModel: SelectedTaskViewModel by activityViewModels { myFactory() }
    lateinit var task: ToDoItem
    private var isNewTask by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false)

        viewModel.isNewTaskLiveData.observe(viewLifecycleOwner){
            isNewTask = it
        }

        viewModel.selectedTaskLiveData.observe(viewLifecycleOwner){
            task = it.copy()
            setTaskView()
        }


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
        showPopupMenu(it)
    }

    private val saveButtonListener = OnClickListener {
        viewModel.saveTask(task)
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
            task.setTaskText(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }

    private val pickDateButtonListener = OnClickListener {
        with(binding){
            task.setDeadline(getUnixTime(datePicker.dayOfMonth, datePicker.month, datePicker.year))
        }
        setDeadlineTextView()
        binding.datePickerContainer.visibility = View.GONE
    }

    private val cancelPickDateButtonListener = OnClickListener {
        binding.datePickerContainer.visibility = View.GONE
    }

    private val dateSwitcherListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            binding.datePickerContainer.visibility = View.VISIBLE
        }
        else {
            binding.deadlineDateTextView.visibility = View.INVISIBLE
            task.removeDeadline()
        }
    }

    private fun goBackToList(){
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun setDeleteButtonView(){
        if(isNewTask){
            binding.deleteButton.setTextColor(resources.getColor(R.color.blue))
            binding.deleteButton.isEnabled = false
        }
    }

    private fun setSwitcherView(){
        if (task.hasDeadline()){
            binding.dateSwitcher.setOnCheckedChangeListener(null)
            binding.dateSwitcher.isChecked = true
            binding.dateSwitcher.setOnCheckedChangeListener(dateSwitcherListener)
        }
    }

    private fun setDeadlineTextView(){
        if (task.hasDeadline()){
            binding.deadlineDateTextView.visibility = View.VISIBLE
            binding.deadlineDateTextView.text = getFormattedDate(task.getDeadline())
        }
        else{
            binding.deadlineDateTextView.visibility = View.INVISIBLE
        }
    }

    private fun setEditFieldView(){
        binding.editTextField.setText(task.getTaskText())
    }

    private fun setImportanceView(){
        when(task.getImportance()){
            ToDoItem.Importance.DEFAULT -> binding.importanceStateTextView.text = getString(R.string.No)
            ToDoItem.Importance.LOW -> binding.importanceStateTextView.text = getString(R.string.Low)
            ToDoItem.Importance.HIGH -> binding.importanceStateTextView.text = getString(R.string.High)
        }
    }

    private fun setTaskView(){
        setEditFieldView()
        setImportanceView()
        setDeadlineTextView()
        setSwitcherView()
        setDeleteButtonView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showPopupMenu(view: View){
        val popupWindow = PopupWindow(view.context)

        val inflater = LayoutInflater.from(view.context)

        val contentView = inflater.inflate(R.layout.importance_pop_up_menu, null)

        popupWindow.contentView = contentView
        setListenersOnPopupMenuItems(popupWindow.contentView, popupWindow)
        setCloseWhenTouchOutsideEvent(popupWindow)

        popupWindow.showAsDropDown(view, 0, 0)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setCloseWhenTouchOutsideEvent(popupWindow: PopupWindow){
        popupWindow.isOutsideTouchable = true
        popupWindow.setTouchInterceptor { _, event ->
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                popupWindow.dismiss()
                true
            } else {
                false
            }
        }
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
        task.setImportance(importance)
        setImportanceView()
    }
}