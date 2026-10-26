package com.movix.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.movix.app.database.MovixDatabase
import com.movix.app.navigation.AppNavHost
import com.movix.app.repository.InventarioRepository
import com.movix.app.ui.theme.MovixTheme
import com.movix.app.viewmodel.InventarioViewModel
import com.movix.app.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = MovixDatabase.obtenerBaseDatos(applicationContext)
        val inventarioRepository = InventarioRepository(database.productoDao(),
        movimientoDao = database.movimientoDao())

        val inventarioViewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(InventarioViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return InventarioViewModel(inventarioRepository) as T
                }
                throw IllegalArgumentException("Clase ViewModel desconocida")
            }
        }

        setContent {
            MovixTheme {
                val loginViewModel: LoginViewModel = viewModel()
                val inventarioViewModel: InventarioViewModel = viewModel(factory = inventarioViewModelFactory)

                AppNavHost(
                    loginViewModel = loginViewModel,
                    inventarioViewModel = inventarioViewModel
                )
            }
        }
    }
}