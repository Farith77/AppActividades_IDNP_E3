package com.example.app_actividades_idnp_e3.data.local.entity

import android.provider.CalendarContract
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val category: String = "General",
    val dueDate: Long, // Guardamos la fecha en milisegundos (Timestamp) para facilitar el ordenamiento
    val reminderDaysBefore: Int, // 0 si no hay recordatorio, o el número de días antes
    val isCompleted: Boolean = false, // Para filtrar las "pendientes"
    val hasReminders: Boolean = true
)