package com.movix.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.movix.app.ui.screen.PantallaCatalogo
import com.movix.app.ui.screen.PantallaLogin
// import com.movix.app.ui.screen.PantallaCatalogo
// import com.movix.app.ui.screen.PantallaMovimiento
// import com.movix.app.ui.screen.PantallaAlertas
import com.movix.app.viewmodel.LoginViewModel
import com.movix.app.viewmodel.InventarioViewModel

@Composable
fun AppNavHost(
    loginViewModel: LoginViewModel,
    inventarioViewModel: InventarioViewModel
) {
    val navController = rememberNavController()


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {

            if (rutaActual != Rutas.Login.ruta) {
                NavigationBar {
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Catálogo.ruta,
                        onClick = {
                            navController.navigate(Rutas.Catálogo.ruta) {
                                launchSingleTop = true // Evita apilar la misma pantalla varias veces[cite: 31]
                            }
                        },
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Catálogo") },
                        label = { Text("Catálogo") }
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Movimiento.ruta,
                        onClick = {
                            navController.navigate(Rutas.Movimiento.ruta) {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Refresh, contentDescription = "Movimientos") },
                        label = { Text("Movimientos") }
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Alertas.ruta,
                        onClick = {
                            navController.navigate(Rutas.Alertas.ruta) {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Notifications, contentDescription = "Alertas") },
                        label = { Text("Alertas") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.Login.ruta, // La app arranca en el Login (RF-01)[cite: 3]
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Rutas.Login.ruta) {
                PantallaLogin(
                    viewModel = loginViewModel,
                    onIngresarExitoso = {
                        // Al ingresar con éxito, navegamos al catálogo y limpiamos el historial atrás si es necesario
                        navController.navigate(Rutas.Catálogo.ruta) {
                            popUpTo(Rutas.Login.ruta) { inclusive = true }
                        }
                    }
                )
            }


            composable(route = "catalogo") {
                PantallaCatalogo(
                    viewModel = inventarioViewModel,
                    onProductoClick = { _ ->
                        // Acción al hacer clic en un producto (opcional por ahora)
                    }
                )
            }


            composable(Rutas.Movimiento.ruta) {
                Surface(modifier = Modifier.padding(innerPadding)) {
                    Text(text = "Pantalla de Movimientos - En construcción")
                }
            }


            composable(Rutas.Alertas.ruta) {
                Surface(modifier = Modifier.padding(innerPadding)) {
                    Text(text = "Pantalla de Alertas - En construcción")
                }
            }
        }
    }
}