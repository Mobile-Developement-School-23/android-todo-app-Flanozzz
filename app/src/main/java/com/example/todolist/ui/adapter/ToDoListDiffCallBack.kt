package com.example.todolist.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.todolist.data.model.ToDoItem

class ToDoListDiffCallBack(
    private val oldList: List<ToDoItem>,
    private val newList: List<ToDoItem>
) : DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }

}