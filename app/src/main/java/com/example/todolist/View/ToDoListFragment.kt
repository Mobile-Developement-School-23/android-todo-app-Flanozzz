package com.example.todolist.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.Adapter.ToDoListAdapter
import com.example.todolist.IOnTaskTouchListener
import com.example.todolist.Model.ToDoItem
import com.example.todolist.R
import com.example.todolist.ViewModel.SelectedTaskViewModel
import com.example.todolist.ViewModel.ToDoListViewModel
import com.example.todolist.databinding.FragmentToDoListBinding

class ToDoListFragment : Fragment() {

    private val dataModel: SelectedTaskViewModel by activityViewModels()
    private val viewModel: ToDoListViewModel by viewModels()
    private lateinit var adapter: ToDoListAdapter
    private lateinit var binding: FragmentToDoListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentToDoListBinding.inflate(inflater, container, false)

        adapter = ToDoListAdapter(actionListener)

        val layoutManager = object : LinearLayoutManager(requireContext()){
            override fun canScrollVertically(): Boolean {
                return false
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }
        binding.toDoListRecycleView.layoutManager = layoutManager
        binding.toDoListRecycleView.adapter = adapter
        binding.toDoListRecycleView.isNestedScrollingEnabled = false

        viewModel.viewableTasks.observe(viewLifecycleOwner){ list ->
            binding.toDoListRecycleView.post{
                adapter.toDoList = list
            }
        }

        viewModel.showOnlyUnfinishedStateLiveData.observe(viewLifecycleOwner){
            viewModel.setViewableTasksByState(it)
            setEyeIcon(it)
        }

        binding.addTaskButton.setOnClickListener(addTaskButtonListener)
        binding.eyeButton.setOnClickListener(eyeButtonListener)

        setNumberOfCompletedTasksText(viewModel.getCompletedTasksCount())

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setNumberOfCompletedTasksText(count: Int){
        val descText = requireContext().getString(R.string.done)
        binding.doneInfoTextView.text = "$descText $count/${viewModel.getTotalTasksCount()}"
    }

    private fun setEyeIcon(isOpen: Boolean){
        val iconRes = if(isOpen){
            R.drawable.ic_baseline_visibility_off_24
        } else{
            R.drawable.visibility
        }
        binding.eyeButton.setImageResource(iconRes)
    }

    private val eyeButtonListener = OnClickListener {
        viewModel.changeShowOnlyUnfinishedState()
    }

    private val addTaskButtonListener = OnClickListener{
        dataModel.createTask()
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_toDoListFragment2_to_editTaskFragment2)
    }

    private val actionListener = object : IOnTaskTouchListener {
        override fun onChangeButtonClick(id: String) {
            dataModel.selectTask(id)
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_toDoListFragment2_to_editTaskFragment2)
        }

        override fun onCheckboxClick(task: ToDoItem, state: Boolean) {
            val newTask = task.copy(isDone = state)
            viewModel.changeTask(newTask)
            setNumberOfCompletedTasksText(viewModel.getCompletedTasksCount())
        }

        override fun onTaskMove(task: ToDoItem, moveBy: Int) {
            viewModel.moveTask(task, moveBy)
        }

        override fun onTaskDelete(task: ToDoItem) {
            viewModel.deleteTask(task)
            setNumberOfCompletedTasksText(viewModel.getCompletedTasksCount())
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveTasks()
    }
}