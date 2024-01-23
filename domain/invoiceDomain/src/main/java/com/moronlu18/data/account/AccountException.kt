package com.moronlu18.data.account


//Lo hacemos clase sellada
//RunTimeException, excepción genérica
sealed class AccountException(message : String = ""):RuntimeException(message) {
    class InvalidEmailFormat: AccountException("Email con formato inválido");
}