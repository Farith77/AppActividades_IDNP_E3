package com.example.app_actividades_idnp_e3

import android.app.Application
import com.example.app_actividades_idnp_e3.data.local.AppDatabase
import com.example.app_actividades_idnp_e3.data.repository.ActivitiesRepository

class ActivitiesApplication : Application() {
    // Inicialización "perezosa" (lazy): solo se crean cuando se necesitan
    // y se comparte la misma instancia en toda la app.
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { ActivitiesRepository(database.activityDao()) }
}