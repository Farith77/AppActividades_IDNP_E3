package com.example.app_actividades_idnp_e3.data.repository

import com.example.app_actividades_idnp_e3.data.local.dao.ActivityDao
import com.example.app_actividades_idnp_e3.data.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

class ActivitiesRepository(private val activityDao: ActivityDao) {

    // Retorna un Flow para que la UI se actualice automáticamente al haber cambios
    val allPendingActivities: Flow<List<ActivityEntity>> = activityDao.getPendingActivities()

    suspend fun insert(activity: ActivityEntity) {
        activityDao.insertActivity(activity)
    }

    suspend fun update(activity: ActivityEntity) {
        activityDao.updateActivity(activity)
    }

    suspend fun delete(activity: ActivityEntity) {
        activityDao.deleteActivity(activity)
    }

    suspend fun getById(id: Int): ActivityEntity? {
        return activityDao.getActivityById(id)
    }
}