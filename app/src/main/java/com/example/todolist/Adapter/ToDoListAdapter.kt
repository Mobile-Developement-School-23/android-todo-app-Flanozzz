package com.example.todolist.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.IOnTaskTouchListener
import com.example.todolist.Model.ToDoItem
import com.example.todolist.R
import com.example.todolist.Utils.*
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
) : RecyclerView.Adapter<ToDoListAdapter.ToDoItemViewHolder>(){

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
        setImportanceIcon(toDoItem.getImportance(), holder.binding.importanceIcon)

        holder.binding.taskInfoContainer
            .setOnClickListener{
                actionListener.onChangeButtonClick(toDoItem.getId())
            }

        holder.binding.taskInfoContainer
            .setOnLongClickListener{
                val popupWindow = PopupMenuCreator.showPopupMenu(it, R.layout.task_actions_menu)
                setListenersOnPopupMenuItems(popupWindow, toDoItem)
                return@setOnLongClickListener true

            }
        holder.binding.taskInfoCheckBox
            .setOnCheckedChangeListener {
                    _, isChecked ->
                run {
                    actionListener.onCheckboxClick(toDoItem, isChecked)
                }
            }
        holder.binding.infoTaskButton
            .setOnClickListener{
                PopupMenuCreator.showPopupMenu(it, createPopupMenuLayout(it.context, toDoItem))
            }
    }

    override fun getItemCount(): Int = toDoList.size

    private fun createPopupMenuLayout(context: Context, task: ToDoItem) : LinearLayoutCompat{
        val layout = PopupMenuCreator.createPopupMenuLayoutContainer(context)
        val textColor = getAndroidAttrTextColor(context, android.R.attr.textColor)
        val dateOfCreateString = context.getString(R.string.date_of_create)
        val dateOfChangeString = context.getString(R.string.date_of_change)
        val dateOfCreateText = "$dateOfCreateString: ${getFormattedDate(task.getDateOfCreate())}"
        val dateOfChangeText = "$dateOfChangeString: ${getFormattedDate(task.getDateOfChange())}"

        val dateOfCreateField = PopupMenuCreator
            .createPopupMenuField(context, dateOfCreateText, textColor)
        val dateOfChangeField = PopupMenuCreator
            .createPopupMenuField(context, dateOfChangeText, textColor)

        if(task.hasDeadline()){
            val deadlineString = context.getString(R.string.deadline)
            val deadlineText = "$deadlineString: ${getFormattedDate(task.getDeadline())}"
            val deadlineField = PopupMenuCreator
                .createPopupMenuField(context, deadlineText, textColor)
            layout.addView(deadlineField)
        }

        layout.addView(dateOfCreateField)
        layout.addView(dateOfChangeField)
        return layout
    }

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

    private fun setListenersOnPopupMenuItems(popupWindow: PopupWindow, task: ToDoItem){
        popupWindow.contentView.findViewById<TextView>(R.id.moveUpButton).setOnClickListener{
            actionListener.onTaskMove(task, -1)
            popupWindow.dismiss()
        }
        popupWindow.contentView.findViewById<TextView>(R.id.moveDownButton).setOnClickListener{
            actionListener.onTaskMove(task, 1)
            popupWindow.dismiss()
        }
        popupWindow.contentView.findViewById<TextView>(R.id.deleteButton).setOnClickListener{
            actionListener.onTaskDelete(task)
            popupWindow.dismiss()
        }
    }
}