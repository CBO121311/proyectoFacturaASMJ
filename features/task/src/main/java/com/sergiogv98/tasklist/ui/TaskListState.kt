package com.sergiogv98.tasklist.ui

import com.moronlu18.data.task.Task

sealed class TaskListState {
    data object NoData: TaskListState()
    data object Success : TaskListState()
    data class Loading(val value: Boolean) : TaskListState()
}