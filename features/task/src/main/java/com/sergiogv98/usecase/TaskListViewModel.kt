package com.sergiogv98.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moronlu18.accounts.entity.Task
import com.moronlu18.accounts.network.ResourceList
import com.moronlu18.accounts.repository.TaskProvider
import com.sergiogv98.tasklist.ui.TaskListState
import kotlinx.coroutines.launch

class TaskListViewModel: ViewModel() {

    private var state = MutableLiveData<TaskListState>()

    fun getTaskList(firstCharge: Boolean){
        viewModelScope.launch {
            lateinit var result: Any

            if (firstCharge) {
                state.value = TaskListState.Loading(true)
                result = TaskProvider.getTaskList()
                state.value = TaskListState.Loading(false)
            } else {
                result = TaskProvider.getTaskList()
            }

            when(result){
                is ResourceList.Success<*> -> state.value = TaskListState.Success(result.data as ArrayList<Task>)
                is ResourceList.Error -> state.value = TaskListState.NoData
            }
        }
    }

    fun getTasks(): List<Task> {
        return TaskProvider.getTasks()
    }

    fun deleteTask(position: Int){
        TaskProvider.taskDataSet.removeAt(position)
    }

    fun getState(): LiveData<TaskListState> {
        return state;
    }
}