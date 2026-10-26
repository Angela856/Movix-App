package com.movix.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.movix.app.viewmodel.LoginViewModel

@Composable
fun PantallaLogin(
    viewModel: LoginViewModel,
    onLoginExitoso: () -> Unit
) {
    val estado by viewModel.estadoFormulario.collectAsState()

    LaunchedEffect(estado.usuarioSeleccionado) {
        if (estado.usuarioSeleccionado != null) {
            onLoginExitoso()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Inicio de Sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = estado.correo,
            onValueChange = { viewModel.onCorreoChanged(it) },
            label = { Text("Usuario o ID (Ej: U01)") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Usuario") },
            isError = estado.error != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = estado.contrasenia,
            onValueChange = { viewModel.onContraseniaChanged(it) },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = estado.error != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (estado.error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = estado.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { viewModel.validarEIniciarSesion() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }
    }
}