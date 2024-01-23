package com.moronlu18.invoice

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.moronlu18.invoice.data.preferences.UserPreferencesRepository
import com.moronlu18.invoice.ui.preferences.DataStorePreferencesRepository

//Creamos un object para que no crear una instancia en cada fragment.
//Locator te da los objetos que tu necesitas no lo hace.
//Te facilita lo que necesita (se vuelve a subdividir).
//El que lo hace el repositorio.

object Locator {
    public var application: Application? = null

    //inline hace una variable inmovil en el mismo momento. Y hace las dos operaciones a la vez.
     public inline val requireApplication
        get() = application ?: error("Missing call: initWith(application)")

    //Esto debe iniciar para poder crear el contexto de los datos (?)
    fun initWith(application: Application) {
        this.application = application
    }

    //Solo el repositorio de la aplicación
    //user_preferences es un xml y este XML es accesible al repositorio.
    //Y todos preguntaran a este locator.

    //crea un fichero que se acaba de guardar en files-> datastore
    private val Context.userStore by preferencesDataStore(name = "user")
    private val Context.settingsStore by preferencesDataStore(name = "settings")

    //lazy =Se inicialice la primera que lo llames.
    //Este es único para todos.
    val userPreferencesRepository by lazy {
        //UserPreferencesRepository(application.userStore) //para evitar el nulo se hace requiereApplication.
        UserPreferencesRepository(requireApplication.userStore)
    }

    val settingsPreferencesRepository by lazy {
        DataStorePreferencesRepository(requireApplication.settingsStore)
    }
}