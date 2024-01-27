package com.moronlu18.repository


import com.moronlu18.data.account.User
import com.moronlu18.network.Resource
import com.moronlu18.network.ResourceList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.ArrayList


/**
Esta clase es accesible en tod0 el proyecto. No se puede crear objetos de esta clase
constructor privado. Y tiene un objeto que contiene el listado de usuarios.
 */


//El repositorio actualmente es estático
class UserRepositoryQuitar private constructor() { //Estático
    companion object {

        var dataSet: MutableList<User> = mutableListOf()

        init {
            //initDataSetUser()
        }

        private fun initDataSetUser() {
            dataSet.add(User("Alejandro", "abc@hotmail.es"))
            dataSet.add(User("Koba", "cbd@hotmail.es"))
            dataSet.add(User("Zanahoria", "zanah@hotmail.es"))
            dataSet.add(User("Rim", "rim@hotmail.es"))
            dataSet.add(User("Mallorca", "123cab@hotmail.es"))
            dataSet.add(User("Paella", "paella@hotmail.com"))
            dataSet.add(User("Cebolla","op@hotmail.es"))
            dataSet.add(User("Rabano", "mesa@gmail.com"))
        }


        /**
         *             dataSet.add(User("Alejandro", Email("abc@hotmail.es")))
         *             dataSet.add(User("Koba", Email("cbd@hotmail.es")))
         *             dataSet.add(User("Zanahoria", Email("zanah@hotmail.es")))
         *             dataSet.add(User("Rim", Email("rim@hotmail.es")))
         *             dataSet.add(User("Mallorca", Email("123cab@hotmail.es")))
         *             dataSet.add(User("Paella", Email("paella@hotmail.com")))
         *             dataSet.add(User("Cebolla", Email("op@hotmail.es")))
         *             dataSet.add(User("Rabano", Email("mesa@gmail.com")))
         */

        //hay que hacer un resource.sucess que se añade en Account.create (creacion de un Account)

        /**
         * La función que se pregunta a Firebase /Room (Sqlite por el usuario)
         */
        suspend fun login(email: String, password: String): Resource {
            //Este código se ejecuta en un hilo específico para operaciones entrada/salida IO

            withContext(Dispatchers.IO) {
                delay(3000)
            }

            return Resource.Error(Exception("El password es incorrecto"))
        }

        /**
         * Esta función simula una consulta asíncrono y devuelve el conjunto de
         * usuarios de la aplicación
         */
        suspend fun getUserList(): ResourceList {
            //Añadimos return, que devuelve la última línea de código
            return withContext(Dispatchers.IO) {
                delay(2000)
                when {
                    dataSet.isEmpty() -> ResourceList.Error(Exception("No hay datos"))
                    //Hacer un cast
                    else -> ResourceList.Success(dataSet as ArrayList<User>)

                }
            }
        }

        /*/**
         * Esta función simula una consulta asíncrono y devuelve el conjunto de
         * usuarios de la aplicación
         */
        suspend fun getUserList(): ResourceList {
            //Añadimos return, que devuelve la última línea de código
            return withContext(Dispatchers.IO) {
                delay(2000)
                when {
                    dataSet.isEmpty() -> ResourceList.Error(Exception("No hay datos"))
                    //Hacer un cast
                    else -> ResourceList.Success(dataSet as ArrayList<User>)

                }
            }
        }*/

        fun addUser(user: User) {
            dataSet.add(user)
        }

        suspend fun existEmailUser(user: User): Resource {

            return withContext(Dispatchers.IO) {
                delay(1500)

                if (dataSet.any { it.email == user.email }) {

                    Resource.Error(Exception("Ya existe este email en el repositorio"))
                } else {
                    Resource.Success(user)
                }
            }
        }
    }
}
