package com.sergiogv98.usecase

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sergiogv98.tasklist.ui.TaskState

const val TAG = "ViewModel"

class TaskViewModel : ViewModel() {

    var taskName = MutableLiveData<String>()

    private var state = MutableLiveData<TaskState>()

    fun validateTask() {
        when {
            TextUtils.isEmpty(taskName.value) -> state.value = TaskState.TitleIsMandatory
            else -> state.value = TaskState.OnSuccess
        }
    }


    fun getState(): LiveData<TaskState> {
        return state
    }
}