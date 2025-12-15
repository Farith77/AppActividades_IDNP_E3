package com.example.app_actividades_idnp_e3

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_actividades_idnp_e3.service.ActivityForegroundService
import com.example.app_actividades_idnp_e3.ui.navigation.AppNavigation
import com.example.app_actividades_idnp_e3.ui.theme.App_ActividadesIDNPE3Theme
import com.example.app_actividades_idnp_e3.ui.viewmodel.ActivitiesViewModel
import com.example.app_actividades_idnp_e3.ui.viewmodel.ActivitiesViewModelFactory

class MainActivity : ComponentActivity() {

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            // Aquí puedes manejar la respuesta, por ahora no hacemos nada
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Solicitar permiso de notificaciones si es Android 13 o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Iniciar el servicio en primer plano
        val serviceIntent = Intent(this, ActivityForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }

        setContent {
            App_ActividadesIDNPE3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Obtener dependencias
                    val application = applicationContext as ActivitiesApplication
                    val repository = application.repository
                    val viewModel: ActivitiesViewModel = viewModel(
                        factory = ActivitiesViewModelFactory(repository)
                    )

                    // Llamar a la navegación
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}
