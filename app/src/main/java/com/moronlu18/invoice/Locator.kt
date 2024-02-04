package com.moronlu18.invoice

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.util.Log
import androidx.core.content.res.ResourcesCompat
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

    val appResources: Resources
        get() = requireApplication.resources

    fun convertirRecursoABitmap(resourceId: Int): Bitmap {
        val options = BitmapFactory.Options()
        return BitmapFactory.decodeResource(appResources, resourceId, options)
    }

    /*private fun getbitMap(imageView: ImageView): Bitmap {
        val bitmap = Bitmap.createBitmap(imageView.width, imageView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        imageView.draw(canvas)
        return bitmap
    }*/

    fun getResourceBitmapV2(resourceId: Int): Bitmap {
        val context = requireApplication

        val options = BitmapFactory.Options()
        return BitmapFactory.decodeResource(context.resources, resourceId,options)
    }

    fun getResourceUri(resourceId: Int): Uri? {
        val context = requireApplication
        val uri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(context.packageName)
            .appendPath(context.resources.getResourceTypeName(resourceId))
            .appendPath(context.resources.getResourceEntryName(resourceId))
            .build()

        return uri
    }
}