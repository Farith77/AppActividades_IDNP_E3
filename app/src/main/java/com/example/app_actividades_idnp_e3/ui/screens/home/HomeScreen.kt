package com.example.app_actividades_idnp_e3.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_actividades_idnp_e3.data.local.entity.ActivityEntity
import com.example.app_actividades_idnp_e3.ui.components.ActivityItem
import com.example.app_actividades_idnp_e3.ui.viewmodel.ActivitiesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ActivitiesViewModel,
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit
) {
    val activities by viewModel.allActivities.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currentFilter by viewModel.filterOption.collectAsState()

    var isSearchActive by remember { mutableStateOf(false) }
    var activityToDelete by remember { mutableStateOf<ActivityEntity?>(null) }
    var isMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF9FAFB),
        topBar = {
            // CAMBIO 1: La barra superior ahora es simple (solo iconos o buscador)
            TopAppBar(
                title = {
                    if (isSearchActive) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { viewModel.onSearchQueryChanged(it) },
                            placeholder = { Text("Buscar actividad...") },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    // Si NO estamos buscando, dejamos el título vacío en la barra
                    // (porque el título grande estará abajo, en el cuerpo)
                },
                navigationIcon = {
                    if (isSearchActive) {
                        IconButton(onClick = {
                            isSearchActive = false
                            viewModel.onSearchQueryChanged("")
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Cerrar búsqueda")
                        }
                    } else {
                        // Icono Menú (Filtros)
                        Box {
                            IconButton(onClick = { isMenuExpanded = true }) {
                                Icon(Icons.Default.Menu, contentDescription = "Filtros", tint = Color(0xFF374151))
                            }
                            // ... (Tu DropdownMenu existente va aquí igual que antes) ...
                            DropdownMenu(
                                expanded = isMenuExpanded,
                                onDismissRequest = { isMenuExpanded = false }
                            ) {
                                listOf("Todas", "Universidad", "Casa", "Trabajo", "Otros").forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category) },
                                        onClick = {
                                            viewModel.setFilter(category)
                                            isMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                actions = {
                    if (isSearchActive) {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Limpiar", tint = Color.Gray)
                            }
                        }
                    } else {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color(0xFF374151))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF9FAFB))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFF4F46E5),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Actividad")
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // CAMBIO 2: El Título Grande ahora es el primer ITEM de la lista
                // Esto permite que tenga todo el espacio que quiera y haga scroll
                item {
                    if (!isSearchActive) { // Solo mostramos el título grande si NO estamos buscando
                        Column(modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)) {
                            Text(
                                text = "Mis Actividades",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 30.sp, // Tamaño grande como en el diseño
                                color = Color(0xFF111827)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            // Subtítulo
                            val subtitle = if (currentFilter == "Todas") {
                                "Tienes ${activities.size} pendientes"
                            } else {
                                "Filtrando por: $currentFilter (${activities.size})"
                            }
                            Text(
                                text = subtitle,
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    } else {
                        // Espacio pequeño si estamos buscando para que no se pegue al borde
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Lista de actividades
                items(activities) { activity ->
                    ActivityItem(
                        activity = activity,
                        onEditClick = { onEditClick(activity.id) },
                        onDeleteClick = { activityToDelete = activity }
                    )
                }
            }

            // Mensaje de lista vacía
            if (activities.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text(
                        text = "No hay actividades pendientes",
                        color = Color.Gray
                    )
                }
            }
        }

        // Diálogo de eliminar (igual que antes)
        if (activityToDelete != null) {
            AlertDialog(
                onDismissRequest = { activityToDelete = null },
                title = { Text("Eliminar Actividad") },
                text = { Text("¿Deseas eliminar '${activityToDelete?.title}'?") },
                confirmButton = {
                    TextButton(onClick = {
                        activityToDelete?.let { viewModel.deleteActivity(it) }
                        activityToDelete = null
                    }) { Text("Eliminar", color = Color.Red) }
                },
                dismissButton = {
                    TextButton(onClick = { activityToDelete = null }) { Text("Cancelar") }
                }
            )
        }
    }
}