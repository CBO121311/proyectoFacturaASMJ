package com.sergiogv98.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moronlu18.data.task.Task
import com.sergiogv98.tasklist.ui.TaskListState
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData
import com.moronlu18.invoice.Locator
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
                else -> {
                    allTask = when (Locator.settingsPreferencesRepository.getSettingValue("tasksort", "id")) {
                        "id" -> taskRepositoryBD.getTaskListById().asLiveData()
                        "name_customer_asc" -> taskRepositoryBD.getTaskOrderByCustomerName().asLiveData()
                        "name_customer_desc" -> taskRepositoryBD.getTaskOrderByCustomerNameDesc().asLiveData()
                        "name_task" -> taskRepositoryBD.getTaskOrderByName().asLiveData()
                        else -> taskRepositoryBD.getTaskList().asLiveData()
                    }

                    state.value = TaskListState.Success
                }
            }
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