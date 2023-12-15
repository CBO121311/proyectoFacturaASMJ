package com.moronlu18.accounts.entity


//Para hacer comparaciones hay que poner el Comparable.
/**
 * Al utilizar data class se implmenta de for automática el método equals, toString, copy y HashCode
 * Teniendo en cuanta las propiedades declaradas en el constructor primerario/principal.
 *
 */
/*
Si quitamos la palabras no sabremos si los objetos son iguales

 */
data class User(val name: String, val surname: String, val email: String) : Comparable<User> {
    //Siempre se compara el propio objeto con otro.
    //Si ordenador A B C (A siempre será menor)
    //Objeto < other <0
    //Si son iguales = 0
    //Objeto > other  >0
    //Establecemos orden natura
    override fun compareTo(other: User): Int {
        return name.compareTo(other.name)
    }

}
