package com.sergiogv98.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.task.Task
import com.moronlu18.repository.TaskRepositoryBD
import com.sergiogv98.tasklist.ui.TaskDetailState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskDetailViewModel : ViewModel() {

    private var state = MutableLiveData<TaskDetailState>()
    var customerName = MutableLiveData<String>()
    var taskName = MutableLiveData<String>()
    var taskStatus = MutableLiveData<String>()
    var typeTask = MutableLiveData<String>()
    var dateCreation = MutableLiveData<String>()
    var dateEnd = MutableLiveData<String>()
    var taskDescription = MutableLiveData<String>()
    private var taskRepositoryBD = TaskRepositoryBD()

    /*
    fun getCustomerPhoto(customerId: Int): Customer {
        return taskRepositoryBD.getCustomerById(customerId) ?: Customer() // Ajusta según tu implementación
    }*/

    fun getCustomer(customerId: CustomerId): Customer? {
        return taskRepositoryBD.getCustomerById(customerId)
    }

    fun delete(task: Task) {
        viewModelScope.launch (Dispatchers.IO){
            taskRepositoryBD.delete(task)
        }
    }

    fun onSuccess(){
        state.value = TaskDetailState.OnSuccess
    }

    fun getState(): LiveData<TaskDetailState>{
        return state
    }

}