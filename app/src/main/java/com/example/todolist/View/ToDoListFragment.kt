package com.example.todolist.View

import android.os.Bundle
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
    ): View? {
        binding = FragmentToDoListBinding.inflate(inflater, container, false)

        adapter = ToDoListAdapter(object : IOnTaskTouchListener {
            override fun onChangeButtonClick(id: String) {
                dataModel.selectTask(id)
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_toDoListFragment2_to_editTaskFragment2)
            }
        })

        val layoutManger = LinearLayoutManager(requireContext())
        binding.toDoListRecycleView.layoutManager = layoutManger
        binding.toDoListRecycleView.adapter = adapter

        viewModel.toDoItemsLiveData.observe(viewLifecycleOwner){
            adapter.toDoList = it
        }

        binding.addTaskButton.setOnClickListener(addTaskButtonListener)

        return binding.root
    }

    private val addTaskButtonListener = OnClickListener{
        dataModel.createTask()
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_toDoListFragment2_to_editTaskFragment2)
    }

}