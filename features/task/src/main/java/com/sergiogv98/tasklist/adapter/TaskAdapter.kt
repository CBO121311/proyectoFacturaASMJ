package com.sergiogv98.tasklist.adapter

import com.moronlu18.data.task.Task
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.repository.CustomerProvider
import com.moronlu18.tasklist.R
import com.moronlu18.tasklist.databinding.ItemTaskBinding

class TaskAdapter(
    private val listener: OnTaskClick
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var dataset = arrayListOf<Task>()
    private var selectedPosition = RecyclerView.NO_POSITION
    private var isAscendingOrder = true

    interface OnTaskClick{
        fun taskClick(position: Int)
        fun taskOnLongClick(view: View, position: Int, task: Task)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TaskViewHolder(layoutInflater.inflate(R.layout.item_task, parent, false))
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)
    }

    fun update(newDataSet: ArrayList<Task>) {
        dataset = newDataSet
        notifyDataSetChanged()
    }

    fun deleteTask(task: Task){
        dataset.remove(task)
        notifyDataSetChanged()
    }

    fun toggleSortOrder() {
        isAscendingOrder = !isAscendingOrder
        sortTasks()
        notifyDataSetChanged()
    }

    private fun sortTasks() {
        if (isAscendingOrder) {
            dataset.sortBy { it.nomTask }
        } else {
            dataset.sortByDescending { it.nomTask }
        }
        notifyDataSetChanged()
    }

    fun clearSelection(){
        selectedPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(private val view: View): RecyclerView.ViewHolder(view){

        private val binding = ItemTaskBinding.bind(view)
        fun bind(task: Task){
            with(binding){
                taskClientName.text = CustomerProvider.getCustomerNameById(task.customerId.value)
                taskName.text = task.nomTask
                taskDescription.text = task.descTask
                taskEndDate.text = task.dateFinalization.toString()
                taskCreationDate.text = task.dateCreation.toString()

                root.setOnClickListener {
                    listener.taskClick(adapterPosition)
                }

                root.setOnLongClickListener {
                    selectedPosition = adapterPosition
                    listener.taskOnLongClick(view, adapterPosition, task)
                    notifyDataSetChanged()
                    true
                }
            }
        }

    }
}