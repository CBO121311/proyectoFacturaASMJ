package com.sergiogv98.tasklist.ui

sealed class TaskCreationState {

    data object TitleIsMandatory : TaskCreationState()
    data object NonExistentTarea : TaskCreationState()
    data object CustomerUnspecified : TaskCreationState()
    data object InvalidId : TaskCreationState()
    data object IncorrectDateRange : TaskCreationState()
    data object OnSuccess : TaskCreationState()
}