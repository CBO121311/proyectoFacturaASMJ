package com.moronlu18.account.usecase

import com.moronlu18.accounts.entity.Account

sealed class SignInState {
    data object EmailEmptyError : SignInState()
    data object EmailFormatError : SignInState()
    data object PasswordEmptyError : SignInState()
    data object PasswordFormatError : SignInState()
    data class AuthencationError(var message: String) : SignInState()
    //data class Success(var account: Resource) : SignInState()
    data class Success(var account: Account?) : SignInState()
    data class Loading(var value: Boolean) : SignInState()
}