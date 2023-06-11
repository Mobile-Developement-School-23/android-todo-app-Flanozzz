package com.example.todolist.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.IOnTaskTouchListener
import com.example.todolist.Model.ToDoItem
import com.example.todolist.databinding.TaskBinding

class UserDiffCallBack(
    private val oldList: List<ToDoItem>,
    private val newList: List<ToDoItem>
) : DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.getId() == newItem.getId()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }

}

class ToDoListAdapter(
    private val actionListener: IOnTaskTouchListener
) : RecyclerView.Adapter<ToDoListAdapter.ToDoItemViewHolder>() {


    var toDoList: List<ToDoItem> = emptyList()
    set(value) {
        val diffCallBack = UserDiffCallBack(field, value)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        field = value
        diffResult.dispatchUpdatesTo(this)
    }

    class ToDoItemViewHolder(
        val binding: TaskBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TaskBinding.inflate(inflater, parent, false)

        return ToDoItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) {
        val toDoItem = toDoList[position]
        holder.binding.taskInfoTextView.text = toDoItem.getTaskText()
        holder.binding.taskInfoCheckBox.isChecked = toDoItem.isDone()
        holder.binding.changeTaskButton.setOnClickListener{actionListener.onChangeButtonClick(toDoItem.getId())}
    }

    override fun getItemCount(): Int = toDoList.size
}