package com.sergiogv98.tasklist.adapter

import com.moronlu18.accounts.entity.Task
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.tasklist.R
import com.moronlu18.tasklist.databinding.ItemTaskBinding

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTaskBinding.bind(view)

    private lateinit var onClickListener: (Task) -> Unit
    private lateinit var onClickDeleted: (Int) -> Unit
    private lateinit var onClickEdit: (Int) -> Unit

    fun render(
        task: Task,
        onClickListener: (Task) -> Unit,
        onClickDeleted: (Int) -> Unit,
        onClickEdit: (Int) -> Unit
    ) {
        this.onClickListener = onClickListener
        this.onClickDeleted = onClickDeleted
        this.onClickEdit = onClickEdit

        binding.taskClientName.text = CustomerProvider.getCustomerNameById(task.customerID.id)
        binding.taskName.text = task.nomTask
        binding.taskDescription.text = task.descTask
        binding.taskCreationDate.text = task.dateCreation.toString().substring(0, task.dateCreation.toString().lastIndexOf("T"))
        binding.taskEndDate.text = task.dateFinalization.toString().substring(0, task.dateFinalization.toString().lastIndexOf("T"))

        itemView.setOnClickListener { onClickListener(task) }
        itemView.setOnLongClickListener {
            showContextMenu(it)
            true  // Indica que el evento ha sido manejado correctamente
        }
    }

    private fun showContextMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.menu_pop, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_edit -> {
                    onClickEdit(adapterPosition)
                    true
                }
                R.id.menu_delete -> {
                    onClickDeleted(adapterPosition)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}