package com.moronlu18.account.usecase

import com.moronlu18.data.account.User

sealed class UserListState(){

    data object NoDataError: UserListState()
    data class Success (val dataset: ArrayList<User>): UserListState()
    data class Loading(val value:Boolean): UserListState()

}
