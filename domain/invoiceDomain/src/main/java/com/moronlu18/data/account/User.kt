package com.moronlu18.data.account

import androidx.room.Entity
import androidx.room.PrimaryKey


//Para hacer comparaciones hay que poner el Comparable./*
//Si quitamos lo de data, no sabremos si los objetos son iguales
/**
 * Al utilizar data class se implmenta de for automática el método equals, toString, copy y HashCode
 * Teniendo en cuanta las propiedades declaradas en el constructor primerario/principal.
 *
 */

@Entity
data class User(@PrimaryKey val name: String, val email: String) : Comparable<User> {
    override fun compareTo(other: User): Int {
        return name.lowercase().compareTo(other.name.lowercase())
    }
}


//Siempre se compara el propio objeto con otro.
//Si ordenador A B C (A siempre será menor)
//Objeto < other <0
//Si son iguales = 0
//Objeto > other  >0
//Establecemos orden natura
