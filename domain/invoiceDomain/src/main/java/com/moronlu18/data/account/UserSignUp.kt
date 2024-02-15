package com.moronlu18.data.account

import com.moronlu18.data.base.Email

data class UserSignUp(
    val name: String,
    val email: String,
    val tipoUsuario: TypeUser,
    val perfil: TypePerfil,
    val password:String
){

    fun toUser(): User {
        return User(name, Email(email).toString())
    }
}


