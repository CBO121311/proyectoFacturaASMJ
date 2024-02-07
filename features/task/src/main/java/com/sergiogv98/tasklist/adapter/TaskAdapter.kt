package com.sergiogv98.tasklist.adapter

import com.moronlu18.data.task.Task
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.repository.TaskRepositoryBD
import com.moronlu18.tasklist.R
import com.moronlu18.tasklist.databinding.ItemTaskBinding
import com.sergiogv98.utils.TaskUtils

class TaskAdapter(
    private val listener: OnTaskClick
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TASK_COMPARATOR) {

    private var isAscendingOrder = true

    private var selectedPosition = RecyclerView.NO_POSITION

    interface OnTaskClick {
        fun taskClick(task: Task)
        fun taskOnLongClick(view: View, task: Task)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TaskViewHolder(layoutInflater.inflate(R.layout.item_task, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun sortId(){
        val sortedList = currentList.sortedBy { it.id }
        submitList(sortedList)
    }


    /*

    fun sortName(){
        val sortedList = currentList.sortedBy { it.nomTask.lowercase() }
        submitList(sortedList)
    }

    fun sortCustomerNameAscent(){
        val sortedList = currentList.sortedBy { it.customerId.value }
        submitList(sortedList)
    }

    fun sortCustomerNameDescent(){
        val sortedList = currentList.sortedByDescending { it.customerId.value }
        submitList(sortedList)
    }*/

    fun clearSelection() {
        submitList(currentList)
    }

    fun toggleSortOrder() {
        isAscendingOrder = !isAscendingOrder
        val sortedList = if (isAscendingOrder) currentList.sortedBy { it.nomTask }
        else currentList.sortedByDescending { it.nomTask }
        submitList(sortedList)
    }

    inner class TaskViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemTaskBinding.bind(view)

        fun bind(task: Task) {
            with(binding) {
                taskClientName.text = TaskRepositoryBD().getCustomerById(task.customerId)?.name ?: "Fallo"
                taskName.text = task.nomTask
                taskDescription.text = task.descTask
                taskEndDate.text = TaskUtils().convertInstantToDateText(task.dateFinalization)
                taskCreationDate.text = TaskUtils().convertInstantToDateText(task.dateCreation)

                root.setOnClickListener {
                    listener.taskClick(task)
                }

                root.setOnLongClickListener {
                    selectedPosition = adapterPosition
                    listener.taskOnLongClick(view, task)
                    true
                }
            }
        }
    }

    companion object{
        private val TASK_COMPARATOR = object :DiffUtil.ItemCallback<Task>(){
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.nomTask == newItem.nomTask
            }

        }
    }
}


