package com.sergiogv98.usecase

import androidx.lifecycle.ViewModel
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.task.Task
import com.moronlu18.repository.CustomerProvider
import com.moronlu18.repository.CustomerProvider.Companion.getCustomerNameById
import com.moronlu18.repository.CustomerProvider.Companion.getCustomerbyID
import com.moronlu18.repository.TaskProvider

class TaskDetailViewModel : ViewModel() {

    fun getTaskDataSet(): MutableList<Task> {
        return TaskProvider.taskDataSet
    }

    /**
     * TODO, Cambiado para el tema de la foto
     * Versión Anterior:
     *  fun getCustomerPhoto(customerId: Int): Int {
     *         return com.moronlu18.inovice.R.drawable.kiwituxedo
     *
     *         //getCustomerPhotoById(customerId)
     *     }
     *
     *
     */
    fun getCustomerPhoto(customerId: Int): Customer {
        return getCustomerbyID(customerId)!!
    }

    fun getPositionByTask(task: Task): Int{
        return TaskProvider.getPositionByTask(task)
    }


}