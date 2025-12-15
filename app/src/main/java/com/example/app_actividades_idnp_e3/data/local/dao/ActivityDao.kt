package com.example.app_actividades_idnp_e3.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.app_actividades_idnp_e3.data.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    // Requisito: "Ver una lista de actividades pendientes según orden de prioridad (fecha cumplimiento)"
    @Query("SELECT * FROM activities WHERE isCompleted = 0 ORDER BY dueDate ASC")
    fun getPendingActivities(): Flow<List<ActivityEntity>>

    // Requisito: "Agregar actividades"
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity)

    // Requisito: "Modificar una actividad"
    @Update
    suspend fun updateActivity(activity: ActivityEntity)

    // Requisito: "Eliminar una actividad"
    @Delete
    suspend fun deleteActivity(activity: ActivityEntity)

    // Útil para buscar una específica por ID si es necesario
    @Query("SELECT * FROM activities WHERE id = :id")
    suspend fun getActivityById(id: Int): ActivityEntity?
}