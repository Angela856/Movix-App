package com.movix.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.movix.app.navigation.AppNavHost
import com.movix.app.ui.theme.MovixTheme
import com.movix.app.viewmodel.LoginViewModel
import com.movix.app.viewmodel.InventarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MovixTheme {

                val loginViewModel: LoginViewModel = viewModel()
                val inventarioViewModel: InventarioViewModel = viewModel()


                AppNavHost(
                    loginViewModel = loginViewModel,
                    inventarioViewModel = inventarioViewModel
                )
            }
        }
    }
}