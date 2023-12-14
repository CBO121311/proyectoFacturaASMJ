package com.sergiogv98.tasklist.adapter

import com.moronlu18.accounts.entity.Task
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.tasklist.databinding.ItemTaskBinding

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemTaskBinding.bind(view)

    fun render(
        task: Task,
        onClickListener: (Task) -> Unit,
        onClickDeleted: (Int) -> Unit,
        onClickEdit: (Int) -> Unit
    ) {
        binding.taskClientName.text = CustomerProvider.getCustomerNameById(task.clientID)
        binding.taskName.text = task.nomTask
        binding.taskDescription.text = task.descTask
        binding.taskCreationDate.text = task.fechCreation.toString().substring(0, task.fechCreation.toString().lastIndexOf("T"))
        binding.taskEndDate.text = task.fechFinalization.toString().substring(0, task.fechFinalization.toString().lastIndexOf("T"))
        binding.taskButtonDelete.setOnClickListener { onClickDeleted (adapterPosition) }
        binding.taskButtonEdit.setOnClickListener { onClickEdit?.invoke(adapterPosition) }
        itemView.setOnClickListener { onClickListener(task) }
    }
}