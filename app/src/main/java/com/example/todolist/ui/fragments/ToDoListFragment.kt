package com.example.todolist.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.todolist.ui.adapter.ToDoListAdapter
import com.example.todolist.ui.adapter.IOnTaskTouchListener
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.R
import com.example.todolist.ToDoApp
import com.example.todolist.ui.viewModels.SelectedTaskViewModel
import com.example.todolist.ui.viewModels.ToDoListViewModel
import com.example.todolist.databinding.FragmentToDoListBinding
import com.example.todolist.di.fragment.ToDoListFragmentComponent
import com.example.todolist.ui.adapter.NonScrollableLinearLayoutManager
import com.example.todolist.ui.viewModels.ViewModelFactory
import com.example.todolist.utils.getCurrentUnixTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ToDoListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val selectedTaskViewModel: SelectedTaskViewModel by activityViewModels{viewModelFactory}
    private val toDoListViewModel: ToDoListViewModel by activityViewModels{viewModelFactory}
    private lateinit var binding: FragmentToDoListBinding
    private lateinit var adapter: ToDoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentToDoListBinding.inflate(inflater, container, false)

        setupDependencies()

        setupRecycleViewAdapter()
        setupObservers()
        setListeners()

        return binding.root
    }

    private fun setupDependencies(){
        (requireContext().applicationContext as ToDoApp)
            .appComponent
            .toDoListFragmentComponent()
            .inject(this)
    }

    private fun setListeners(){
        binding.addTaskButton.setOnClickListener(addTaskButtonListener)
        binding.eyeButton.setOnClickListener(eyeButtonListener)
        binding.swipeRefreshLayout.setOnRefreshListener(onRefreshListener)
    }

    private fun setupRecycleViewAdapter(){
        adapter = ToDoListAdapter(actionListener)
        val layoutManager = NonScrollableLinearLayoutManager(requireContext())
        binding.toDoListRecycleView.layoutManager = layoutManager
        binding.toDoListRecycleView.adapter = adapter
        binding.toDoListRecycleView.isNestedScrollingEnabled = false
    }

    private fun setupObservers(){
        lifecycleScope.launch {
            toDoListViewModel.toDoItems.collect{ list ->
                val completedCount = list.filter { it.isDone }.size
                val totalCount = list.size
                setNumberOfCompletedTasksText(completedCount, totalCount)
            }
        }

        lifecycleScope.launch {
            toDoListViewModel.showOnlyUnfinishedStateFlow.collect{
                toDoListViewModel.setViewableTasksByState(it)
                setEyeIcon(it)
            }
        }

        lifecycleScope.launch {
            toDoListViewModel.viewableTasksStateFlow.collect{ list ->
                binding.toDoListRecycleView.post{
                    adapter.toDoList = list
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setNumberOfCompletedTasksText(doneCount: Int, totalCount: Int){
        val descText = requireContext().getString(R.string.done)
        binding.doneInfoTextView.text = "$descText $doneCount/$totalCount"
    }

    private fun setEyeIcon(isOpen: Boolean){
        val iconRes = if(isOpen){
            R.drawable.ic_baseline_visibility_off_24
        } else {
            R.drawable.visibility
        }
        binding.eyeButton.setImageResource(iconRes)
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        toDoListViewModel.viewModelScope.launch(Dispatchers.IO) {
            toDoListViewModel.syncData()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private val eyeButtonListener = OnClickListener {
        toDoListViewModel.changeShowOnlyUnfinishedState()
    }

    private val addTaskButtonListener = OnClickListener{
        selectedTaskViewModel.createTask()
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_toDoListFragment2_to_editTaskFragment2)
    }

    private val actionListener = object : IOnTaskTouchListener {
        override fun onChangeButtonClick(id: String) {
            selectedTaskViewModel.selectTask(id)
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_toDoListFragment2_to_editTaskFragment2)
        }

        override fun onCheckboxClick(task: ToDoItem) {
            val newTask = task.copy(isDone = !task.isDone, dateOfChange = getCurrentUnixTime())
            toDoListViewModel.changeTask(newTask)
        }

        override fun onTaskDelete(task: ToDoItem) {
            toDoListViewModel.deleteTask(task)
        }
    }
}