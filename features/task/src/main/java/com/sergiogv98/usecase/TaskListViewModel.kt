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

    fun getTaskList(){
        viewModelScope.launch {
            var result = TaskProvider.getTaskList()

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