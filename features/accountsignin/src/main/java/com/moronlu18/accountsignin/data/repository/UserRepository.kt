package com.moronlu18.accountsignin.data.repository

import com.moronlu18.accountsignin.data.model.User

//import com.example.pruebasconclientes.data.model.User

/**
    Esta clase es accesible en tod0 el proyecto. No se puede crear objetos de esta clase
    constructor privado. Y tiene un objeto que contiene el listado de usuarios.
 */

class UserRepository private constructor(){


    companion object{
        val  dataSet: MutableList<User> = initDataSetUser()

        private fun initDataSetUser(): MutableList<User> {
            var dataSet: MutableList<User> = ArrayList()
            dataSet.add(User("Alejandro", "López", "cb2@hotmail.es"))
            dataSet.add(User("Paella", "1234", "vfrv2@hotmail.es"))
            dataSet.add(User("Cebolla", "Veeee", "cbb@hotmail.es"))
            dataSet.add(User("Rabano", "Ra", "rf@hotmail.es"))
            dataSet.add(User("Alejandro", "López", "cb2@hotmail.es"))
            dataSet.add(User("Paella", "1234", "vfrv2@hotmail.es"))
            dataSet.add(User("Cebolla", "Veeee", "cbb@hotmail.es"))
            dataSet.add(User("Rabano", "Ra", "rf@hotmail.es"))
            return dataSet

        }
    }
}