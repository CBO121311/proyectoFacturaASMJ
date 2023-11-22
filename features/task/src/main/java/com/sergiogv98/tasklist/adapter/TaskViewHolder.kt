package com.sergiogv98.tasklist.adapter

import com.moronlu18.accounts.entity.Task
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.tasklist.databinding.ItemTaskBinding

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemTaskBinding.bind(view)

    fun render(task: Task, onClickListener: (Task) -> Unit, onClickDeleted: (Int) -> Unit) {
        binding.taskClientName.text = task.nomClient
        binding.taskName.text = task.nomTask
        binding.taskDescription.text = task.descTask
        binding.taskCreationDate.text = task.fechCreation
        binding.taskEndDate.text = task.fechFinalization
        binding.taskButtonDelete.setOnClickListener { onClickDeleted (adapterPosition) }
        itemView.setOnClickListener { onClickListener(task) }
    }
}