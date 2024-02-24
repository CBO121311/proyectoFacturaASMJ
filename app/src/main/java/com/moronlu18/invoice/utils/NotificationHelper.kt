package com.moronlu18.invoice.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.moronlu18.invoice.R

/**
 * Creates Notification Channel (required for API level >= 26) before sending any notification.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun createNotificationChannel(channelId: String,context: Context) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channel = NotificationChannel(
        channelId,
        "Important Notification Channel",
        NotificationManager.IMPORTANCE_HIGH,
    ).apply {
        description = "This notification contains important announcement, etc."
    }
    notificationManager.createNotificationChannel(channel)
}

/**
 * Esta función esta sacada de clase, inicialmente recibia además por parámetro un pending intent
 * pero he decidido quitarlo debido a que no lo necesito, esto provoca que al pinchar la notificación
 * no veamos ninguna acción
 */
@SuppressLint("MissingPermission")
fun sendNotification(channelId: String,context: Context, title: String, textContext: String){

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(textContext)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    with(NotificationManagerCompat.from(context)) {
        notify(getUniqueId(), builder.build())
    }
}
private fun getUniqueId() = (System.currentTimeMillis()%10000).toInt()