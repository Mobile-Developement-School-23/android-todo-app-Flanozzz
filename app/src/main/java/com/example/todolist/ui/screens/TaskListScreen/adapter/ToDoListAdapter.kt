package com.example.todolist.ui.screens.TaskListScreen.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.model.ToDoItem
import com.example.todolist.R
import com.example.todolist.utils.*
import com.example.todolist.databinding.TaskBinding

class ToDoListAdapter (
    private val actionListener: IOnTaskTouchListener
) : RecyclerView.Adapter<ToDoListAdapter.ToDoItemViewHolder>(){

    var toDoList: List<ToDoItem> = emptyList()
    set(value) {
        val diffCallBack = ToDoListDiffCallBack(field, value)
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
        holder.binding.taskInfoTextView.text = toDoItem.text
        holder.binding.taskInfoCheckBox.isChecked = toDoItem.isDone
        setStrikeThruTextFlag(toDoItem.isDone, holder.binding.taskInfoTextView)
        setImportanceIcon(toDoItem.importance, holder.binding.importanceIcon)
        setListeners(holder, toDoItem, position)
    }

    private fun setListeners(holder: ToDoItemViewHolder, toDoItem: ToDoItem, position: Int){
        with(holder.binding){
            rootContainer.setOnClickListener{
                actionListener.onChangeButtonClick(toDoItem.id)
            }

            rootContainer.setOnLongClickListener{
                showPopupMenuAction(it, toDoItem)
                return@setOnLongClickListener true
            }

            taskInfoCheckBox.setOnClickListener{
                setStrikeThruTextFlag(!toDoItem.isDone, taskInfoTextView)
                actionListener.onCheckboxClick(toDoItem)
            }

            infoTaskButton.setOnClickListener{
                showPopupMenuInfo(it, toDoItem)
            }
        }
    }

    private fun setStrikeThruTextFlag(state: Boolean, textView: TextView){
        if(state){
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    private fun showPopupMenuAction(view: View, task: ToDoItem){
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menu.add(Menu.NONE, DELETE, Menu.NONE, view.context.getString(R.string.Delete))

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                DELETE -> {
                    actionListener.onTaskDelete(task)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun showPopupMenuInfo(view: View, task: ToDoItem){
        val popupMenu = PopupMenu(view.context, view)

        val dateOfCreateString = view.context.getString(R.string.date_of_create)
        val dateOfChangeString = view.context.getString(R.string.date_of_change)
        val dateOfCreateText = "$dateOfCreateString: ${unixTimeToDMY(task.dateOfCreate)}"
        val dateOfChangeText = "$dateOfChangeString: ${unixTimeToDMY(task.dateOfChange)}"

        popupMenu.menu.add(Menu.NONE, DATE_OF_CREATE, Menu.NONE, dateOfCreateText)
        popupMenu.menu.add(Menu.NONE, DATE_OF_CHANGE, Menu.NONE, dateOfChangeText)

        popupMenu.show()
    }

    override fun getItemCount(): Int = toDoList.size

    private fun setImportanceIcon(importance: ToDoItem.Importance, importanceIcon: ImageView){
        when(importance){
            ToDoItem.Importance.DEFAULT -> importanceIcon.visibility = View.GONE
            ToDoItem.Importance.LOW -> {
                importanceIcon.visibility = View.VISIBLE
                importanceIcon.setImageResource(R.drawable.ic_low_important)
            }
            ToDoItem.Importance.HIGH -> {
                importanceIcon.visibility = View.VISIBLE
                importanceIcon.setImageResource(R.drawable.ic_important)
            }
        }
    }

    companion object{
        const val DELETE = 1
        const val DATE_OF_CREATE = 2
        const val DATE_OF_CHANGE = 3
    }
}