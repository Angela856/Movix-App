package com.movix.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.movix.app.ui.screen.PantallaAlertas
import com.movix.app.ui.screen.PantallaBienvenida
import com.movix.app.ui.screen.PantallaCatalogo
import com.movix.app.ui.screen.PantallaDetalleProducto
import com.movix.app.ui.screen.PantallaEscaner
import com.movix.app.ui.screen.PantallaFormularioProducto
import com.movix.app.ui.screen.PantallaLogin
import com.movix.app.ui.screen.PantallaMovimiento
import com.movix.app.viewmodel.InventarioViewModel
import com.movix.app.viewmodel.LoginViewModel

@Composable
fun AppNavHost(
    loginViewModel: LoginViewModel,
    inventarioViewModel: InventarioViewModel
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    val estadoLogin by loginViewModel.estadoFormulario.collectAsState()
    val nombreUsuarioActivo = estadoLogin.usuarioSeleccionado?.nombre ?: "Usuario"

    Scaffold(
        bottomBar = {
            if (rutaActual != Rutas.Login.ruta && rutaActual != "bienvenida") {
                NavigationBar {
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Catálogo.ruta,
                        onClick = {
                            navController.navigate(Rutas.Catálogo.ruta) {
                                popUpTo(Rutas.Catálogo.ruta) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Catálogo") },
                        label = { Text("Catálogo") }
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Movimiento.ruta,
                        onClick = {
                            navController.navigate(Rutas.Movimiento.ruta) {
                                popUpTo(Rutas.Catálogo.ruta) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Refresh, contentDescription = "Movimientos") },
                        label = { Text("Movimientos") }
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Alertas.ruta,
                        onClick = {
                            navController.navigate(Rutas.Alertas.ruta) {
                                popUpTo(Rutas.Catálogo.ruta) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
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
            startDestination = Rutas.Login.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Rutas.Login.ruta) {
                PantallaLogin(
                    viewModel = loginViewModel,
                    onLoginExitoso = {
                        navController.navigate("bienvenida") {
                            popUpTo(Rutas.Login.ruta) { inclusive = true }
                        }
                    }
                )
            }

            composable("bienvenida") {
                PantallaBienvenida(
                    onNavegarACatalogo = {
                        navController.navigate(Rutas.Catálogo.ruta)
                    },
                    onNavegarAAlertas = {
                        navController.navigate(Rutas.Alertas.ruta)
                    },
                    onNavegarAFormulario = {
                        navController.navigate(Rutas.FormularioProducto.ruta)
                    },
                    onNavegarAEscaner = {
                        navController.navigate(Rutas.Escaner.ruta)
                    }
                )
            }

            composable(Rutas.Escaner.ruta) {
                PantallaEscaner(
                    viewModel = inventarioViewModel,
                    onProductoClick = { producto ->
                        navController.navigate("${Rutas.DetalleProducto.ruta}/${producto.codigo}")
                    },
                    onAgregarProductoConCodigo = { codigo ->
                        navController.navigate("${Rutas.FormularioProducto.ruta}?codigo=$codigo")
                    },
                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Rutas.Catálogo.ruta) {
                PantallaCatalogo(
                    viewModel = inventarioViewModel,
                    onProductoClick = { producto ->
                        navController.navigate("${Rutas.DetalleProducto.ruta}/${producto.codigo}")
                    },
                    onAgregarProductoClick = {
                        navController.navigate(Rutas.FormularioProducto.ruta)
                    }
                )
            }

            composable(Rutas.Movimiento.ruta) {
                PantallaMovimiento(
                    viewModel = inventarioViewModel,
                    usuarioActivoNombre = nombreUsuarioActivo
                )
            }

            composable(Rutas.Alertas.ruta) {
                val productos by inventarioViewModel.productosFiltrados.collectAsState()
                PantallaAlertas(
                    productos = productos
                )
            }

            composable(
                route = "${Rutas.DetalleProducto.ruta}/{codigoProducto}",
                arguments = listOf(navArgument("codigoProducto") { type = NavType.StringType })
            ) { backStackEntry ->
                val codigoProducto = backStackEntry.arguments?.getString("codigoProducto")
                val productos by inventarioViewModel.productosFiltrados.collectAsState()
                val productoEncontrado = productos.find { it.codigo == codigoProducto }

                if (productoEncontrado != null) {
                    PantallaDetalleProducto(
                        producto = productoEncontrado,
                        onVolver = { navController.popBackStack() }
                    )
                }
            }

            composable(
                route = "${Rutas.FormularioProducto.ruta}?codigo={codigo}",
                arguments = listOf(navArgument("codigo") {
                    type = NavType.StringType
                    defaultValue = ""
                })
            ) { backStackEntry ->
                val codigoInicial = backStackEntry.arguments?.getString("codigo") ?: ""
                PantallaFormularioProducto(
                    viewModel = inventarioViewModel,
                    codigoInicial = codigoInicial,
                    onProductoGuardado = {
                        navController.popBackStack()
                    },
                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}