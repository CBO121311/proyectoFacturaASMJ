package com.sergiogv98.tasklist.ui

import com.moronlu18.accounts.entity.Task

sealed class TaskListState {
    data object NoData: TaskListState()
    data class Success(val dataset: ArrayList<Task>) : TaskListState()
}