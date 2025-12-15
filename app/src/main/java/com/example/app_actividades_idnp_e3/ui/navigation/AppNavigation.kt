package com.example.app_actividades_idnp_e3.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.app_actividades_idnp_e3.ui.screens.detail.AddActivityScreen
import com.example.app_actividades_idnp_e3.ui.screens.home.HomeScreen
import com.example.app_actividades_idnp_e3.ui.viewmodel.ActivitiesViewModel

@Composable
fun AppNavigation(viewModel: ActivitiesViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onAddClick = {
                    // Enviamos -1 para indicar que es NUEVA actividad
                    navController.navigate("add_activity?id=-1")
                },
                onEditClick = { activityId ->
                    // Enviamos el ID real para EDITAR
                    navController.navigate("add_activity?id=$activityId")
                }
            )
        }

        // Definimos que esta ruta acepta un argumento llamado "id"
        composable(
            route = "add_activity?id={id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            // Recuperamos el ID que nos pasaron
            val activityId = backStackEntry.arguments?.getInt("id") ?: -1

            AddActivityScreen(
                viewModel = viewModel,
                activityId = activityId, // Pasamos el ID a la pantalla
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}