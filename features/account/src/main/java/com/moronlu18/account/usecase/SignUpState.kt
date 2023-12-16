package com.moronlu18.account.usecase

import com.moronlu18.accounts.entity.User
import com.moronlu18.accounts.entity.UserSignUp
import com.moronlu18.accounts.network.Resource

sealed class SignUpState {


    data object NameEmptyError : SignUpState()
    data object EmailEmptyError : SignUpState()
    data object EmailFormatError : SignUpState()
    data object PasswordEmptyError : SignUpState()
    data object PasswordNotEquals : SignUpState()
    data class OnSuccess(var user:User) : SignUpState()
    data class Loading(var value: Boolean): SignUpState()
    data class AuthencationError(var message:String):SignUpState()
}