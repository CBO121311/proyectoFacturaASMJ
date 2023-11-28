package com.sergiogv98.tasklist.ui

sealed class TaskState {

    data object TitleIsMandatory : TaskState()
    data object NonExistentTarea : TaskState()
    data object CustomerUnspecified : TaskState()
    data object InvalidId : TaskState()
    data object IncorrectDateRange : TaskState()
    data object OnSuccess : TaskState()
}