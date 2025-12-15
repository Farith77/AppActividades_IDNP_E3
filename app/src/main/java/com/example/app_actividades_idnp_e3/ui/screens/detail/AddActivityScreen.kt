package com.example.app_actividades_idnp_e3.ui.screens.detail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_actividades_idnp_e3.ui.viewmodel.ActivitiesViewModel
import java.util.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityScreen(
    viewModel: ActivitiesViewModel,
    activityId: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // Estados del formulario
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Universidad") }
    var reminderEnabled by remember { mutableStateOf(false) }
    var reminderDays by remember { mutableStateOf("1") }

    // Manejo de Fecha y Hora
    val calendar = Calendar.getInstance()
    var selectedDateMilli by remember { mutableLongStateOf(System.currentTimeMillis()) }

    // Si recibimos un ID válido, cargamos los datos
    LaunchedEffect(activityId) {
        if (activityId != -1) {
            val activity = viewModel.getActivityById(activityId)
            activity?.let {
                title = it.title
                description = it.description
                selectedCategory = it.category
                selectedDateMilli = it.dueDate
                reminderEnabled = it.reminderDaysBefore > 0
                if (reminderEnabled) {
                    reminderDays = it.reminderDaysBefore.toString()
                }
            }
        }
    }

    // Función para mostrar el DatePicker y luego el TimePicker
    fun showDateTimePickers() {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)
                        selectedDateMilli = calendar.timeInMillis
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (activityId == -1) "Nueva Actividad" else "Editar Actividad") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Scroll por si el teclado tapa campos
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Título
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título de la actividad") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 2. Descripción
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            // 3. Selección de Categoría (Chips)
            Text("Categoría", fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()), // Habilita el deslizamiento lateral
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Universidad", "Trabajo", "Casa", "Otros").forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        leadingIcon = if (selectedCategory == category) {
                            { Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null
                    )
                }
            }

            // 4. Selección de Fecha
            Text("Fecha de cumplimiento", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(selectedDateMilli)),
                onValueChange = { },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDateTimePickers() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDateTimePickers() } // Click en todo el campo
            )

            // 5. Recordatorio
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Recordarme antes", modifier = Modifier.weight(1f))
                Switch(checked = reminderEnabled, onCheckedChange = { reminderEnabled = it })
            }

            if (reminderEnabled) {
                OutlinedTextField(
                    value = reminderDays,
                    onValueChange = { if (it.all { char -> char.isDigit() }) reminderDays = it },
                    label = { Text("Días de anticipación") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Botón Guardar / Actualizar
            Button(
                onClick = {
                    if (title.isBlank()) {
                        Toast.makeText(context, "El título es obligatorio", Toast.LENGTH_SHORT).show()
                    } else {
                        val days = if (reminderEnabled) reminderDays.toIntOrNull() ?: 0 else 0

                        if (activityId == -1) {
                            // MODO CREAR
                            viewModel.addActivity(
                                title = title,
                                description = description,
                                date = selectedDateMilli,
                                category = selectedCategory,
                                reminderDays = days,
                                hasReminders = reminderEnabled
                            )
                        } else {
                            // MODO EDITAR (NUEVO)
                            // Creamos la entidad con el ID existente para que Room sepa que es update
                            val updatedActivity = com.example.app_actividades_idnp_e3.data.local.entity.ActivityEntity(
                                id = activityId, // ID IMPORTANTE
                                title = title,
                                description = description,
                                dueDate = selectedDateMilli,
                                category = selectedCategory,
                                reminderDaysBefore = days,
                                hasReminders = reminderEnabled
                            )
                            viewModel.updateActivity(updatedActivity)
                        }
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                // Texto dinámico
                Text(if (activityId == -1) "Guardar Actividad" else "Actualizar Actividad")
            }
        }
    }
}