package com.movix.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.movix.app.model.Producto
import com.movix.app.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioProducto(
    viewModel: InventarioViewModel,
    codigoInicial: String = "",
    onProductoGuardado: () -> Unit,
    onVolver: () -> Unit,
) {
    var codigo by remember(codigoInicial) { mutableStateOf(codigoInicial) }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Anime") }
    var precio by remember { mutableStateOf("") }
    var stockActual by remember { mutableStateOf("") }
    var stockMinimo by remember { mutableStateOf("5") }

    val categorias = listOf("Anime", "Deportes Anime", "Cultura Pop", "Accesorios", "Exclusivos", "Insumos")
    var dropdownCategoriasExpandido by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Producto") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = codigo,
                onValueChange = { codigo = it },
                label = { Text("Código de Producto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = dropdownCategoriasExpandido,
                onExpandedChange = { dropdownCategoriasExpandido = !dropdownCategoriasExpandido }
            ) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownCategoriasExpandido) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = dropdownCategoriasExpandido,
                    onDismissRequest = { dropdownCategoriasExpandido = false }
                ) {
                    categorias.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                categoria = cat
                                dropdownCategoriasExpandido = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = stockActual,
                onValueChange = { stockActual = it },
                label = { Text("Stock Actual") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = stockMinimo,
                onValueChange = { stockMinimo = it },
                label = { Text("Stock Mínimo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val producto = Producto(
                        codigo = codigo.ifBlank { "PRD-${(1000..9999).random()}" },
                        nombre = nombre,
                        descripcion = descripcion,
                        categoria = categoria,
                        tipo = "Estándar",
                        precio = precio.toIntOrNull() ?: 0,
                        stockActual = stockActual.toIntOrNull() ?: 0,
                        stockMinimo = stockMinimo.toIntOrNull() ?: 0
                    )
                    viewModel.agregarProducto(producto) {}
                    onProductoGuardado()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Producto")
            }
        }
    }
}