package com.movix.app.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun BotonEscanerCamara(
    onPermisoConcedido: () -> Unit,
    onSinPermiso: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val actividad = LocalActivity.current
    var mostrarExplicacion by remember { mutableStateOf(false) }

    val lanzadorPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            onPermisoConcedido()
        } else {
            onSinPermiso()
        }
    }

    OutlinedButton(
        onClick = {
            val yaTienePermiso = ContextCompat.checkSelfPermission(
                contexto, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED

            when {
                yaTienePermiso -> {
                    onPermisoConcedido()
                }
                actividad != null && ActivityCompat.shouldShowRequestPermissionRationale(
                    actividad, Manifest.permission.CAMERA
                ) -> {
                    mostrarExplicacion = true
                }
                else -> {
                    lanzadorPermiso.launch(Manifest.permission.CAMERA)
                }
            }
        },
        modifier = modifier
    ) {
        Text("Escanear Código con Cámara")
    }

    if (mostrarExplicacion) {
        AlertDialog(
            onDismissRequest = { mostrarExplicacion = false },
            title = { Text("Permiso de Cámara") },
            text = { Text("Necesitamos la cámara para leer automáticamente el código de barras del producto.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarExplicacion = false
                    lanzadorPermiso.launch(Manifest.permission.CAMERA)
                }) { Text("Continuar") }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarExplicacion = false
                    onSinPermiso()
                }) { Text("Ingresar Manualmente") }
            }
        )
    }
}

