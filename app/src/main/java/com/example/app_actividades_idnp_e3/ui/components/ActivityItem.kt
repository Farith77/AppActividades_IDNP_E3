package com.example.app_actividades_idnp_e3.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_actividades_idnp_e3.data.local.entity.ActivityEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ActivityItem(
    activity: ActivityEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // Definimos colores según la categoría (Hardcoded para el ejemplo visual)
    val categoryColor = when (activity.category.lowercase()) {
        "universidad" -> Color(0xFFE0E7FF) // Fondo azulito/morado claro
        "texto_uni" -> Color(0xFF4338CA)   // Texto azul oscuro
        "casa" -> Color(0xFFDCFCE7)        // Fondo verde claro
        "texto_casa" -> Color(0xFF15803D)  // Texto verde oscuro
        "trabajo" -> Color(0xFFFEF9C3)     // Fondo amarillo claro
        "texto_trabajo" -> Color(0xFFA16207)// Texto amarillo oscuro
        "otros" -> Color(0xFFF3F4F6)       // Gris claro para "Otros
        else -> Color(0xFFF3F4F6)          // Default gris
    }

    val textColor = when (activity.category.lowercase()) {
        "universidad" -> Color(0xFF4338CA)
        "casa" -> Color(0xFF15803D)
        "trabajo" -> Color(0xFFA16207)
        "otros" -> Color(0xFF4B5563)
        else -> Color.Black
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // --- CABECERA: Categoría y Botones de acción ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Chip de Categoría
                Surface(
                    color = categoryColor,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                        // Aquí podrías poner un icono chiquito según la categoría si quieres
                        Text(
                            text = activity.category,
                            style = MaterialTheme.typography.labelMedium,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Botones Editar / Eliminar
                Row {
                    IconButton(onClick = onEditClick, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Gray, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Gray, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- CUERPO: Título y Descripción ---
            Text(
                text = activity.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = activity.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6B7280),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- PIE: Fecha y Recordatorio ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Fecha
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = null,
                    tint = Color(0xFFEF4444), // Rojo suave
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formatTimestamp(activity.dueDate),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFEF4444),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Recordatorio
                if (activity.reminderDaysBefore > 0) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        tint = Color(0xFFD97706), // Ámbar
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${activity.reminderDaysBefore}min antes",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFD97706),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// Función auxiliar para formatear la fecha como en tu imagen "25 nov, 23:59"
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale("es", "ES"))
    return sdf.format(Date(timestamp))
}