package com.example.todolist.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.constraintlayout.utils.widget.ImageFilterView
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
import com.example.todolist.myFactory

class ToDoListFragment : Fragment() {

    private val dataModel: SelectedTaskViewModel by activityViewModels { myFactory() }
    private val viewModel: ToDoListViewModel by viewModels { myFactory() }
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

        viewModel.toDoItemsLiveData.observe(viewLifecycleOwner){ list ->
            adapter.toDoList = if(viewModel.getShowOnlyCompletedState()) list.filter { it.isDone() } else list
        }

        binding.addTaskButton.setOnClickListener(addTaskButtonListener)
        binding.eyeButton.setOnClickListener(eyeButtonListener)

        setNumberOfCompletedTasksText(viewModel.getCompletedTasksCount())

        showTasks(binding.eyeButton)

        return binding.root
    }

    private fun setNumberOfCompletedTasksText(count: Int){
        val descText = requireContext().getString(R.string.done)
        binding.doneInfoTextView.text = "$descText $count/${viewModel.getTotalTasksCount()}"
    }

    private fun showTasks(view: ImageFilterView){
        val iconRes: Int
        if(viewModel.getShowOnlyCompletedState()){
            adapter.toDoList = viewModel.toDoItemsLiveData.value!!.filter { it.isDone() }
            iconRes = R.drawable.ic_baseline_visibility_off_24
        }
        else{
            adapter.toDoList = viewModel.toDoItemsLiveData.value!!
            iconRes = R.drawable.visibility
        }
        view.setImageResource(iconRes)
    }

    private val eyeButtonListener = OnClickListener {
        viewModel.changeState()
        showTasks(it as ImageFilterView)
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
            val newTask = task.copy()
            newTask.setIsDone(state)
            viewModel.changeTask(newTask, false)
            setNumberOfCompletedTasksText(viewModel.getCompletedTasksCount())
        }

        override fun onTaskMove(task: ToDoItem, moveBy: Int) {
            viewModel.moveTask(task, moveBy)
        }

        override fun onTaskDelete(task: ToDoItem) {
            viewModel.deleteTask(task)
        }
    }
}