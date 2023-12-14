package com.sergiogv98.tasklist.adapter

import com.moronlu18.accounts.entity.Task
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.tasklist.R

class TaskAdapter(
    private var taskList: List<Task>,
    private val onClickListener: ((Task) -> Unit)? = null,
    private val onClickDeleted: ((Int) -> Unit)? = null,
    private val onClickEdit: ((Int) -> Unit)? = null,
) : RecyclerView.Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TaskViewHolder(layoutInflater.inflate(R.layout.item_task, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = taskList[position]
        holder.render(item, onClickListener!!, onClickDeleted!!, onClickEdit!!)
    }

    fun update(newDataSet: ArrayList<Task>) {
        taskList = newDataSet
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = taskList.size
}