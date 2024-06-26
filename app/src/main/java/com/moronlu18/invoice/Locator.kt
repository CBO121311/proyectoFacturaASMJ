package com.moronlu18.invoice

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.preferencesDataStore
import com.moronlu18.invoice.data.preferences.UserPreferencesRepository
import com.moronlu18.invoice.ui.preferences.DataStorePreferencesRepository

//Creamos un object para que no crear una instancia en cada fragment.
//Locator te da los objetos que tu necesitas no lo hace.
//Te facilita lo que necesita (se vuelve a subdividir).
//El que lo hace el repositorio.

object Locator {
    public var application: Application? = null

    public inline val requireApplication
        get() = application ?: error("Missing call: initWith(application)")

    fun initWith(application: Application) {
        this.application = application
    }

    private val Context.userStore by preferencesDataStore(name = "user")
    private val Context.settingsStore by preferencesDataStore(name = "settings")

    val userPreferencesRepository by lazy {
        UserPreferencesRepository(requireApplication.userStore)
    }

    val settingsPreferencesRepository by lazy {
        DataStorePreferencesRepository(requireApplication.settingsStore)
    }

    fun getResourceUri(resourceId: Int): Uri? {
        val context = requireApplication

        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(context.packageName)
            .appendPath(context.resources.getResourceTypeName(resourceId))
            .appendPath(context.resources.getResourceEntryName(resourceId))
            .build()
    }

}