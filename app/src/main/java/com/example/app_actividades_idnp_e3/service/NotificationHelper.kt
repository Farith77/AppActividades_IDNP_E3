package com.example.app_actividades_idnp_e3.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.app_actividades_idnp_e3.R

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "activity_monitor_channel"
        const val NOTIFICATION_ID = 1
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Monitoreo de Actividades"
            val descriptionText = "Canal para notificaciones de actividades pendientes"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Crea la notificación persistente para el Foreground Service
    fun createForegroundNotification(): android.app.Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Organizador de Actividades")
            .setContentText("Monitoreando tus actividades pendientes...")
            .setSmallIcon(android.R.drawable.ic_menu_agenda) // Icono por defecto de Android
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    // Crea una notificación normal para avisar de una tarea específica
    fun showActivityNotification(title: String, description: String, activityId: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("¡Recordatorio: $title!")
            .setContentText(description)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(activityId, notification)
    }
}