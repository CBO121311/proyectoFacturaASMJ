package com.sergiogv98.usecase

import androidx.lifecycle.ViewModel
import com.moronlu18.accounts.entity.Task
import com.moronlu18.accounts.repository.CustomerProvider.Companion.getCustomerNameById
import com.moronlu18.accounts.repository.CustomerProvider.Companion.getCustomerPhotoById
import com.moronlu18.accounts.repository.TaskProvider

class TaskDetailViewModel: ViewModel() {

    fun getTaskDataSet(): MutableList<Task> {
        return TaskProvider.taskDataSet
    }

    fun getCustomerPhoto(customerId: Int): Int {
        return getCustomerPhotoById(customerId)
    }

    fun getCustomerName(customerId: Int): String? {
        return getCustomerNameById(customerId)
    }


}