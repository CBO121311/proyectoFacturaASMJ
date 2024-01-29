package com.sergiogv98.tasklist.ui

sealed class TaskDetailState {
    data object OnSuccess: TaskDetailState()
}