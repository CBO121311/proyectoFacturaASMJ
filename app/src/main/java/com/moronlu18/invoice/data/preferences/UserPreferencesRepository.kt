package com.moronlu18.invoice.data.preferences


import androidx.datastore.core.DataStore
//Mira lo de importar
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Esta clase contiene todos los métodos necesrarios para leer y guardar datos del usuario.
 * preferencias en el almacén de datos "user_preferences"
 */

//Porque forma parte del main por eso no se pone en la estructura
class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    /**
     * Método que guarda la información del usuario de Firebase en user_preference
     */

    //lo del asíncrono, esto no pasa nada hacerlo porque son lecturas cortas.
    suspend fun saveUser(email: String, password: String, id: Int) {

        //GlobalScope.launch {  }

        GlobalScope.launch(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[EMAIL] = email
                preferences[PASSWORD] = password
                preferences[ID] = id
            }
        }
        //Utilizo clave valor para leerlo
    }

    fun getEmail(): String {

        //dataStore.data es el flujo y esto cogiendo el Email
        return runBlocking {
            dataStore.data.map { preferences ->
                //Quiero leer dentro de este data
                preferences[EMAIL] ?: "none"
            }.first() //dame el primer valor
        }

        //Lo recogemos cuando utilizamos el collect.
        //Recordamos que lo llamamos desde una corrutina o una función suspendida.

    }

    fun getPassword(): String {
        return runBlocking {
            dataStore.data.map { preferences ->
                //No queremos propagar los nulos.
                preferences[PASSWORD] ?: "none"
            }.first() //dame el primer valor
        }


    }

    //Cuidado con los nulls
    fun savePassword(newPassword: String) {
        runBlocking {
            dataStore.edit { preferences -> preferences[PASSWORD] = newPassword }
        }

    }

    //cada preferencia escribimos que tipo es.
    companion object {
        private val EMAIL = stringPreferencesKey("email")
        private val PASSWORD = stringPreferencesKey("password")
        private val ID = intPreferencesKey("id")
    }
}