package com.sergiogv98.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

const val TAG = "ViewModel"

class TaskViewModel : ViewModel() {

    private var state = MutableLiveData<TaskState>()

    fun getState(): LiveData<TaskState> {
        return state
    }
}