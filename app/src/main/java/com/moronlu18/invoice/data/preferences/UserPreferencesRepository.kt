package com.moronlu18.invoice.data.preferences


import androidx.datastore.core.DataStore
//Mira lo de importar
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

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

        dataStore.edit { preferences ->
            preferences[EMAIL] = email
            preferences[PASSWORD] = password
            preferences[ID] = id
        }
    }

    companion object {
        private val EMAIL = stringPreferencesKey("email")
        private val PASSWORD = stringPreferencesKey("password")
        private val ID = intPreferencesKey("id")
    }
}