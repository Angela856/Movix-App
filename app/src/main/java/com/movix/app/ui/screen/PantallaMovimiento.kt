package com.movix.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.movix.app.model.Movimiento
import com.movix.app.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMovimiento(
    viewModel: InventarioViewModel,
    usuarioActivoNombre: String
) {
    val productos by viewModel.productosFiltrados.collectAsState()
    val estadoForm by viewModel.estadoFormularioMovimiento.collectAsState()
    val movimientos by viewModel.movimientos.collectAsState()
    var dropdownProductosExpandido by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Registrar Movimiento") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ExposedDropdownMenuBox(
                expanded = dropdownProductosExpandido,
                onExpandedChange = { dropdownProductosExpandido = !dropdownProductosExpandido }
            ) {
                val productoSeleccionado = productos.find { it.codigo == estadoForm.productoCodigo }

                OutlinedTextField(
                    value = productoSeleccionado?.let { "${it.nombre} (${it.codigo})" } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Producto*") },
                    placeholder = { Text("Seleccionar un producto") },
                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Lista de Productos") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownProductosExpandido) },
                    isError = estadoForm.errores.productoError != null,
                    supportingText = {
                        estadoForm.errores.productoError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = dropdownProductosExpandido,
                    onDismissRequest = { dropdownProductosExpandido = false }
                ) {
                    if (productos.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No hay productos disponibles") },
                            onClick = { dropdownProductosExpandido = false }
                        )
                    } else {
                        productos.forEach { prod ->
                            DropdownMenuItem(
                                text = { Text("${prod.nombre} — Stock: ${prod.stockActual}") },
                                onClick = {
                                    viewModel.onProductoMovimientoChanged(prod.codigo)
                                    dropdownProductosExpandido = false
                                }
                            )
                        }
                    }
                }
            }

            Text("Tipo de Movimiento", style = MaterialTheme.typography.titleMedium)

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Movimiento.TipoMovimiento.entries.forEach { tipoOp ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = (estadoForm.tipo == tipoOp),
                            onClick = { viewModel.onTipoMovimientoChanged(tipoOp) }
                        )
                        Text(
                            text = when (tipoOp) {
                                Movimiento.TipoMovimiento.ENTRADA -> "Entrada (Llegada de mercadería)"
                                Movimiento.TipoMovimiento.SALIDA_VENTA -> "Salida por Venta"
                                Movimiento.TipoMovimiento.SALIDA_CONSUMO -> "Salida por Consumo"
                                Movimiento.TipoMovimiento.AJUSTE -> "Ajuste de Inventario"
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            OutlinedTextField(
                value = estadoForm.cantidadText,
                onValueChange = { viewModel.onCantidadMovimientoChanged(it) },
                label = { Text("Cantidad*") },
                leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cantidad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = estadoForm.errores.cantidadError != null,
                supportingText = {
                    estadoForm.errores.cantidadError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estadoForm.motivo,
                onValueChange = { viewModel.onMotivoMovimientoChanged(it) },
                label = {
                    Text(
                        if (estadoForm.tipo == Movimiento.TipoMovimiento.AJUSTE)
                            "Motivo del ajuste (Obligatorio)*"
                        else
                            "Motivo / Observación (Opcional)"
                    )
                },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "Motivo") },
                isError = estadoForm.errores.motivoError != null,
                supportingText = {
                    estadoForm.errores.motivoError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.procesarFormularioMovimiento(usuarioActivoNombre) {
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Movimiento")
            }
            Text("Historial de Movimientos", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))

            if (movimientos.isEmpty()) {
                Text("No hay movimientos registrados.")
            } else {
                movimientos.forEach { mov ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Producto: ${mov.codigoProducto} — Tipo: ${mov.tipo}", fontWeight = FontWeight.Bold)
                            Text(text = "Cantidad: ${mov.cantidad} | Motivo: ${mov.motivo.ifBlank { "Sin motivo" }}")
                            Text(text = "Fecha: ${mov.fecha} | Usuario: ${mov.usuario}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}