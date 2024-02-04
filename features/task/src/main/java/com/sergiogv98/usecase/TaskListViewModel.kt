package com.sergiogv98.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.task.Task
import com.moronlu18.network.ResourceList
import com.moronlu18.repository.TaskProvider
import com.moronlu18.invoice.Locator
import com.moronlu18.repository.CustomerProvider
import com.sergiogv98.tasklist.ui.TaskListState
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData
import com.moronlu18.data.base.TaskId
import com.moronlu18.database.InvoiceDatabase
import com.moronlu18.repository.TaskRepositoryBD
import kotlinx.coroutines.Dispatchers

class TaskListViewModel : ViewModel() {

    private var state = MutableLiveData<TaskListState>()
    private var taskRepositoryBD = TaskRepositoryBD()
    var allTask = taskRepositoryBD.getTaskList().asLiveData()

    fun getTaskList() {
        viewModelScope.launch {
            when{
                allTask.value?.isEmpty() == true -> state.value = TaskListState.NoData
                else -> state.value = TaskListState.Success
            }

            /*
            when (result) {
                is ResourceList.Success<*> -> {
                    val task = result.data as ArrayList<Task>

                    when (Locator.settingsPreferencesRepository.getSettingValue("tasksort", "id")) {
                        "id" -> task.sortBy { it.id.value }
                        "name_customer_asc" -> task.sortBy { getCustomer(it.customerId.value - 1) }
                        "name_customer_desc" -> task.sortByDescending { getCustomer(it.customerId.value - 1) }
                        "name_task" -> task.sortBy { it.nomTask }
                    }

                    state.value = TaskListState.Success(task)
                }

                is ResourceList.Error -> state.value = TaskListState.NoData
            }*/
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch (Dispatchers.IO){
            taskRepositoryBD.delete(task)
        }
    }

    fun getState(): LiveData<TaskListState> {
        return state;
    }
}