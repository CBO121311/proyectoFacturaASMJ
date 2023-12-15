package com.moronlu18.accounts.repository


import com.moronlu18.accounts.entity.User
import com.moronlu18.accounts.network.Resource
import com.moronlu18.accounts.network.ResourceList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.ArrayList

//import com.example.pruebasconclientes.data.model.User

/**
Esta clase es accesible en tod0 el proyecto. No se puede crear objetos de esta clase
constructor privado. Y tiene un objeto que contiene el listado de usuarios.
 */


//El repositorio actualmente es estático
class UserRepository private constructor() { //Estático
    //Cuando usa UserRepository. llama a todos los métodos

    companion object {

        var dataSet: MutableList<User> = mutableListOf()

        init {
            initDataSetUser()
        }

        private fun initDataSetUser() {
            dataSet.add(User("Alejandro", "López", "zb2@hotmail.es"))
            dataSet.add(User("Koba", "1234", "vfrv2@hotmail.es"))
            dataSet.add(User("Zanahoria", "Veeee", "cbb@hotmail.es"))
            dataSet.add(User("Rim", "Ra", "uf@hotmail.es"))
            dataSet.add(User("Mallorca", "López", "bb2@hotmail.es"))
            dataSet.add(User("Paella", "1234", "vfrv2@hotmail.es"))
            dataSet.add(User("Cebolla", "Veeee", "cbb@hotmail.es"))
            dataSet.add(User("Rabano", "Ra", "ef@hotmail.es"))
        }


        /**
         * La función que se pregunta a Firebase /Room (Sqlite por el usuario)
         */
        //al ser una corrutina se hace suspend
        suspend fun login(email: String, password: String): Resource {
            //Este código se ejecuta en un hilo específico para operaciones entrada/salida IO
            withContext(Dispatchers.IO) {
                delay(3000)
                /*
                Se ejecutará el código de consulta a Firebase que puede tardar más de 5sg y en ese
                caso se obtiene el error ANR(Android Not Responding) porque puede bloquear la vista

                para garantizar que no falle
                */

                /*return Resource.Success(
                    data = Account.st
                )*/

            }

            return Resource.Error(Exception("El password es incorrecto"))
        }

        /**
         * Esta función simula una consulta asíncrono y devuelve el conjunto de
         * usuarios de la aplicación
         */
        //MutableList<User>
        suspend fun getUserList(): ResourceList {
            //Añadimos return, que devuelve la última línea de código
            return withContext(Dispatchers.IO) {
                delay(2000)
                when{
                    dataSet.isEmpty()-> ResourceList.Error(Exception("No hay datos"))


                    //Hacer un cast
                    else -> ResourceList.Success(dataSet as ArrayList<User>)

                }
            }
           // return dataSet
            //dataSet.find { user.email == it.email }.let { return true }//^^
        }
    }
}
