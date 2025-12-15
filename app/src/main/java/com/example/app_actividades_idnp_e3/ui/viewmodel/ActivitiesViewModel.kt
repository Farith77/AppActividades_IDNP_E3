package com.example.app_actividades_idnp_e3.ui.viewmodel

import android.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app_actividades_idnp_e3.data.local.entity.ActivityEntity
import com.example.app_actividades_idnp_e3.data.repository.ActivitiesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ActivitiesViewModel(private val repository: ActivitiesRepository) : ViewModel() {

    // 1. Estado para el texto de búsqueda
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 2. NUEVO: Estado para el filtro de categoría ("Todas", "Universidad", "Casa", etc.)
    private val _filterOption = MutableStateFlow("Todas")
    val filterOption = _filterOption.asStateFlow()

    // 3. Lógica Maestra: Combinamos Lista + Búsqueda + Filtro
    val allActivities: StateFlow<List<ActivityEntity>> = combine(
        repository.allPendingActivities,
        _searchQuery,
        _filterOption
    ) { list, query, filter ->
        // Primero filtramos la lista cruda
        list.filter { activity ->
            // Condición 1: Coincide con el texto (o el texto está vacío)
            val matchesSearch = activity.title.contains(query, ignoreCase = true)

            // Condición 2: Coincide con la categoría (o el filtro es "Todas")
            val matchesFilter = if (filter == "Todas") true else activity.category == filter

            matchesSearch && matchesFilter
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Funciones para actualizar estados desde la UI
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun setFilter(category: String) {
        _filterOption.value = category
    }

    // --- Funciones CRUD (Igual que antes) ---
    fun addActivity(title: String, description: String, date: Long, category: String, reminderDays: Int, hasReminders: Boolean) {
        viewModelScope.launch {
            val newActivity = ActivityEntity(
                title = title,
                description = description,
                dueDate = date,
                category = category,
                reminderDaysBefore = reminderDays,
                hasReminders = hasReminders
            )
            repository.insert(newActivity)
        }
    }

    fun updateActivity(activity: ActivityEntity) {
        viewModelScope.launch { repository.update(activity) }
    }

    fun deleteActivity(activity: ActivityEntity) {
        viewModelScope.launch { repository.delete(activity) }
    }

    suspend fun getActivityById(id: Int): ActivityEntity? {
        return repository.getById(id)
    }
}

// Factory (Igual que antes)
class ActivitiesViewModelFactory(private val repository: ActivitiesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivitiesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActivitiesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}