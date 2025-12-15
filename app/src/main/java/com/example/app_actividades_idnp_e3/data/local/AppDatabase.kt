package com.example.app_actividades_idnp_e3.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app_actividades_idnp_e3.data.local.dao.ActivityDao
import com.example.app_actividades_idnp_e3.data.local.entity.ActivityEntity

@Database(entities = [ActivityEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun activityDao(): ActivityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_actividades_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}