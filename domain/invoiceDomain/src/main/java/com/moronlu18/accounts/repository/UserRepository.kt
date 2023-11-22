package com.moronlu18.accounts.repository

//import com.example.pruebasconclientes.data.model.User

/**
    Esta clase es accesible en tod0 el proyecto. No se puede crear objetos de esta clase
    constructor privado. Y tiene un objeto que contiene el listado de usuarios.
 */

class UserRepository private constructor(){


    companion object{
        val  dataSet: MutableList<com.moronlu18.accounts.entity.User> = initDataSetUser()
        private fun initDataSetUser(): MutableList<com.moronlu18.accounts.entity.User> {
            var dataSet: MutableList<com.moronlu18.accounts.entity.User> = ArrayList()
            dataSet.add(com.moronlu18.accounts.entity.User("Alejandro", "López", "cb2@hotmail.es"))
            dataSet.add(com.moronlu18.accounts.entity.User("Paella", "1234", "vfrv2@hotmail.es"))
            dataSet.add(com.moronlu18.accounts.entity.User("Cebolla", "Veeee", "cbb@hotmail.es"))
            dataSet.add(com.moronlu18.accounts.entity.User("Rabano", "Ra", "rf@hotmail.es"))
            dataSet.add(com.moronlu18.accounts.entity.User("Alejandro", "López", "cb2@hotmail.es"))
            dataSet.add(com.moronlu18.accounts.entity.User("Paella", "1234", "vfrv2@hotmail.es"))
            dataSet.add(com.moronlu18.accounts.entity.User("Cebolla", "Veeee", "cbb@hotmail.es"))
            dataSet.add(com.moronlu18.accounts.entity.User("Rabano", "Ra", "rf@hotmail.es"))
            return dataSet
        }
    }
}