package com.example.app_actividades_idnp_e3.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.app_actividades_idnp_e3.ActivitiesApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ActivityForegroundService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var notificationHelper: NotificationHelper

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 1. Iniciar el servicio en primer plano (Requisito obligatorio)
        startForeground(NotificationHelper.NOTIFICATION_ID, notificationHelper.createForegroundNotification())

        // 2. Iniciar el monitoreo en segundo plano
        startMonitoring()

        // Si el sistema mata el servicio, intentar reiniciarlo
        return START_STICKY
    }

    private fun startMonitoring() {
        serviceScope.launch {
            val repository = (application as ActivitiesApplication).repository

            // Ciclo de monitoreo simulado
            while (true) {
                // Obtenemos la lista actual de la BD (solo una vez, sin observar cambios constantes)
                val activities = repository.allPendingActivities.first()
                val currentTime = System.currentTimeMillis()

                activities.forEach { activity ->
                    // Lógica simple: Si falta menos de 1 día y tiene recordatorio activado
                    // OJO: Esta es una lógica simple de ejemplo.
                    val timeDiff = activity.dueDate - currentTime
                    // Por ejemplo, si falta menos de 24h y no se ha notificado antes (lógica a refinar)
                    if (timeDiff > 0 && timeDiff < 86400000 && activity.reminderDaysBefore > 0) {
                        notificationHelper.showActivityNotification(
                            activity.title,
                            "Vence pronto: ${activity.description}",
                            activity.id
                        )
                    }
                }

                // Esperar 1 hora antes de volver a chequear (o el tiempo que desees)
                // Para pruebas, puedes poner delay(10000) -> 10 segundos
                delay(60000 * 60)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel() // Limpiamos las corrutinas al destruir el servicio
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // No necesitamos enlazar este servicio a la UI
    }
}