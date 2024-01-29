package com.moronlu18.repository

sealed class UserState{
    data class InsertError(var message:String) :UserState()
    data object Success: UserState()
}
