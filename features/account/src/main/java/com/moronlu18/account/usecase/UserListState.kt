package com.moronlu18.account.usecase

import com.moronlu18.data.account.User

sealed class UserListState(){

    data object NoDataError: UserListState()
    data object Success : UserListState() //Ya tengo los datos en el repositorio.
    data class Loading(val value:Boolean): UserListState()

}
