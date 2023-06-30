package com.example.todolist.views

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.todolist.adapter.ToDoListAdapter
import com.example.todolist.IOnTaskTouchListener
import com.example.todolist.models.ToDoItem
import com.example.todolist.R
import com.example.todolist.viewModels.SelectedTaskViewModel
import com.example.todolist.viewModels.ToDoListViewModel
import com.example.todolist.databinding.FragmentToDoListBinding
import com.example.todolist.deviceIdFactory
import com.example.todolist.repositories.ToDoRepository
import com.example.todolist.utils.getCurrentUnixTime
import com.example.todolist.utils.getRetryToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoListFragment : Fragment() {

    private val dataModel: SelectedTaskViewModel by activityViewModels{deviceIdFactory()}
    private val viewModel: ToDoListViewModel by activityViewModels()
    private lateinit var adapter: ToDoListAdapter
    private lateinit var binding: FragmentToDoListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentToDoListBinding.inflate(inflater, container, false)

        setRecycleViewAdapter()
        launchViewableTasksCollect()
        launchShowOnlyUnfinishedCollect()
        setListeners()
        registerNetworkCallback()

        return binding.root
    }

    private fun registerNetworkCallback(){
        val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                viewModel.syncData()
            }
            override fun onLost(network: Network) {}
        }

        val connectivityManager = requireContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(connectivityCallback)
    }

    private fun setListeners(){
        binding.addTaskButton.setOnClickListener(addTaskButtonListener)
        binding.eyeButton.setOnClickListener(eyeButtonListener)
        binding.swipeRefreshLayout.setOnRefreshListener(onRefreshListener)
    }

    private fun setRecycleViewAdapter(){
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
    }

    private fun launchShowOnlyUnfinishedCollect(){
        lifecycleScope.launch {
            viewModel.showOnlyUnfinishedStateFlow.collect{
                viewModel.setViewableTasksByState(it)
                setEyeIcon(it)
            }
        }
    }

    private fun launchViewableTasksCollect(){
        lifecycleScope.launch {
            viewModel.viewableTasksStateFlow.collect{ list ->
                val completedCount = list.filter { it.isDone }.size
                val totalCount = list.size
                setNumberOfCompletedTasksText(completedCount, totalCount)
                binding.toDoListRecycleView.post{
                    adapter.toDoList = list
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.startCollectCoroutine()
    }

    @SuppressLint("SetTextI18n")
    private fun setNumberOfCompletedTasksText(doneCount: Int, totalCount: Int){
        val descText = requireContext().getString(R.string.done)
        binding.doneInfoTextView.text = "$descText $doneCount/$totalCount"
    }

    private fun setEyeIcon(isOpen: Boolean){
        val iconRes = if(isOpen){
            R.drawable.ic_baseline_visibility_off_24
        } else{
            R.drawable.visibility
        }
        binding.eyeButton.setImageResource(iconRes)
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.syncData()
            binding.swipeRefreshLayout.isRefreshing = false
        }
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

        override fun onCheckboxClick(task: ToDoItem) {
            val newTask = task.copy(isDone = !task.isDone, dateOfChange = getCurrentUnixTime())
            viewModel.changeTask(newTask, getRetryToast(requireContext()))
        }

        override fun onTaskDelete(task: ToDoItem) {
            viewModel.deleteTask(task, getRetryToast(requireContext()))
        }
    }
}