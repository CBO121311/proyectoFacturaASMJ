package com.moronlu18.accounts.entity

import com.moronlu18.accounts.enum_entity.TypePerfil
import com.moronlu18.accounts.enum_entity.TypeUser

data class UserSignUp(
    val name: String,
    val email: String,
    val tipoUsuario: TypeUser,
    val perfil: TypePerfil,
    val password:String
){

    fun toUser(): User {
        return User(name, Email(email))
    }
}


