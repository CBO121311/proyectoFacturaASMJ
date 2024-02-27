package com.moronlu18.invoice.utils

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moronlu18.invoice.Locator
import com.moronlu18.invoice.ui.MainActivity

class NotificationPermisssionRequester(private val activity: ComponentActivity) {

    private val requestPermissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    if (Build.VERSION.SDK_INT >= 33) {
                        //Caso de uso o apartado 5a del diagrama Android Developer. Comprobar que se muestra la opción PermissionRationale
                        if (activity.shouldShowRequestPermissionRationale((Manifest.permission.POST_NOTIFICATIONS))) //5b explicamos al usuario
                            showNotificationPermissionRationale()
                        else
                            showSettingDialog() //Caso de uso o apartado 6 del diagrama Android Developer
                    }

            } else {
                sendNotification(
                    MainActivity.CHANNEL_ID,
                    activity,
                    activity.getString(com.moronlu18.invoice.R.string.notification_example_title),
                    activity.getString(com.moronlu18.invoice.R.string.notification_example_content)
                )
                Locator.settingsPreferencesRepository.putBoolean("key_active_notification", true)

            }
        }

    /**
     * Esta función muestra un cuadro de dialogo que permite al usuario aceptar o bien cancelar la opción de dar permisos.
     * En el caso que el usuario acepte se abre la ventana de configuración de la aplicación
     * donde el usuario debe seleccionar manualmente que permite las notificaciones a la app
     */
    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(
            activity,
            R.style.MaterialAlertDialog_Material3
        )
            .setTitle(activity.getString(com.moronlu18.invoice.R.string.notification_setting_title))
            .setMessage(activity.getString(com.moronlu18.invoice.R.string.notification_setting_message))
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${activity.packageName}")
                activity.startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Esta función se ejecuta cuando el usuario ha rechazado por primera vez asignar el permiso de
     * mostrar notificaciones al usuario
     */
    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(
            activity,
            R.style.MaterialAlertDialog_Material3
        )
            .setTitle(activity.getString(com.moronlu18.invoice.R.string.notification_permission_rationale_title))
            .setMessage(activity.getString(com.moronlu18.invoice.R.string.notification_permission_rationale_message))
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    requestPermissionLauncher?.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            //Si cancela en nuestro código se mostrará siempre showSettingDialog
            .setNegativeButton("Cancel", null)
            .show()
    }


    /**
     * Función que solicita el permiso para crear notificaciones
     */
    fun tryRequest() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        return requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}