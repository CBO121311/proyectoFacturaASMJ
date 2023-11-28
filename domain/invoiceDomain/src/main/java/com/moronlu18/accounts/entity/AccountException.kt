package com.moronlu18.accounts.entity


//Lo hacemos clase sellada
//RunTimeException, excepción genérica
sealed class AccountException(message : String = ""):RuntimeException(message) {
    class InvalidEmailFormat: AccountException("Email con formato inválido");
}