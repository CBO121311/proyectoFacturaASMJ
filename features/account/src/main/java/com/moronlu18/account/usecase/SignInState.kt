package com.moronlu18.account.usecase

import com.moronlu18.accounts.network.Resource

sealed class SignInState {
    data object EmailEmptyError : SignInState()
    data object EmailFormatError : SignInState()
    data object PasswordEmptyError : SignInState()
    data object PasswordFormatError : SignInState()
    data class AuthencationError(var message: String) : SignInState()
    data class Success(var account: Resource) : SignInState()
    data class Loading(var value: Boolean) : SignInState()
}