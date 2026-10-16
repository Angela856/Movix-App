package com.movix.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.movix.app.viewmodel.LoginViewModel

@Composable
fun PantallaLogin(
    viewModel: LoginViewModel,
    onIngresarExitoso: () -> Unit
) {
    // Observamos el estado del formulario desde el ViewModel
    val estadoFormulario by viewModel.estadoFormulario.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión - Movix",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para el correo o usuario
        OutlinedTextField(
            value = estadoFormulario.correo,
            onValueChange = { nuevoCorreo ->
                viewModel.actualizarCorreo(nuevoCorreo)
            },
            label = { Text("Correo electrónico") },
            isError = estadoFormulario.error != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo para la contraseña (opcional según tu lógica de login)
        OutlinedTextField(
            value = estadoFormulario.contrasena,
            onValueChange = { nuevaContrasena ->
                viewModel.actualizarContrasena(nuevaContrasena)
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = estadoFormulario.error != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Muestra el mensaje de error si la validación falla
        estadoFormulario.error?.let { mensajeError ->
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de ingreso
        Button(
            onClick = {
                val exito = viewModel.validarYIngresar()
                if (exito) {
                    onIngresarExitoso()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }
    }
}