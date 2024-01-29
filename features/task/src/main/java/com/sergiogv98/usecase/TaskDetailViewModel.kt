package com.sergiogv98.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.base.TaskId
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.task.Task
import com.moronlu18.data.task.TaskStatus
import com.moronlu18.data.task.TypeTask
import com.moronlu18.repository.CustomerProvider
import com.moronlu18.repository.CustomerProvider.Companion.getCustomerNameById
import com.moronlu18.repository.CustomerProvider.Companion.getCustomerbyID
import com.moronlu18.repository.TaskProvider
import com.sergiogv98.tasklist.ui.TaskDetailState
import java.time.Instant

class TaskDetailViewModel : ViewModel() {

    private var state = MutableLiveData<TaskDetailState>()
    var customerName = MutableLiveData<String>()
    var taskName = MutableLiveData<String>()
    var taskStatus = MutableLiveData<String>()
    var typeTask = MutableLiveData<String>()
    var dateCreation = MutableLiveData<String>()
    var dateEnd = MutableLiveData<String>()
    var taskDescription = MutableLiveData<String>()
    private val repository = TaskProvider

    fun getPositionByTask(task: Task): Int{
        return repository.getPositionByTask(task)
    }

    fun getTaskByPosition(posTask: Int): Task{
        return repository.getTaskPosition(posTask)
    }

    fun getCustomerPhoto(customerId: Int): Customer{
        return getCustomerbyID(customerId)!!
    }

    fun getCustomerName(position: Int): String?{
        return CustomerProvider.getCustomerNameById(position)
    }

    fun onSuccess(){
        state.value = TaskDetailState.OnSuccess
    }

    fun getState(): LiveData<TaskDetailState>{
        return state
    }

}