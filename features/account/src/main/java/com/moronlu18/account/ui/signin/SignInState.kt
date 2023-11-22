package com.moronlu18.account.ui.signin

sealed class SignInState {
    data object EmailEmptyError: SignInState() //Escribe solo el nombre
    data object EmailFormatError: SignInState()
    data object PasswordEmptyError:SignInState()
    data object PasswordFormatError:SignInState()
    data object Success:SignInState()
}