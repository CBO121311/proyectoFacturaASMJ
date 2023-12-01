package com.moronlu18.account.ui.signin

import com.moronlu18.accounts.entity.Account

/*Esto es mi viewModel de SignIn que coge el genérico de Resource.
La clase sellada engloba todos los errores*/
sealed class SignInState {
    object EmailEmptyError : SignInState() //Escribe solo el nombre
    data object EmailFormatError : SignInState()
    data object PasswordEmptyError : SignInState()
    data object PasswordFormatError : SignInState()

    //todos estos son clases
    data class AuthencationError(var message: String) : SignInState()
    data class Success(var account: Account?) : SignInState()
    data class AutethencationError(var message: String) : SignInState()
    /*data object Success:SignInState()
    Yo debo saber lo que sabe pasar la visa
    Quiero que el error aparezca tal como aparezca en FireBase

   data class Success(var account: Resources) : SignInState()
    No se puede poner en plan genérico
    data  class Success(var account: Account):SignInState()*/

    //Se debe crear una clase que contiene un valor booleando que indica si se muestra el Progesbar
    data class Loading(var value: Boolean) : SignInState()
}