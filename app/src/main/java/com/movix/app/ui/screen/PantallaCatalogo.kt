package com.movix.app.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll // <-- 1. Importante para el scroll de categorías
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState // <-- 2. Importante para recordar el estado del scroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.movix.app.model.Producto
import com.movix.app.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogo(
    viewModel: InventarioViewModel,
    onProductoClick: (Producto) -> Unit,
    onAgregarProductoClick: () -> Unit
) {
    val productos by viewModel.productosFiltrados.collectAsState()
    val filtroBusqueda by viewModel.filtroBusqueda.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()

    val categorias = listOf("Todas", "Anime", "Deportes Anime", "Cultura Pop", "Accesorios", "Exclusivos", "Insumos")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Catálogo de Productos") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregarProductoClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Producto"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = filtroBusqueda,
                onValueChange = { viewModel.actualizarFiltroBusqueda(it) },
                label = { Text("Buscar producto o código...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categorias.forEach { cat ->
                    FilterChip(
                        selected = (categoriaSeleccionada == cat),
                        onClick = { viewModel.seleccionarCategoria(cat) },
                        label = { Text(cat) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))


            if (productos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontraron productos.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(
                        items = productos,
                        key = { it.codigo }
                    ) { producto ->
                        ElementoProducto(
                            producto = producto,
                            onClick = { onProductoClick(producto) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ElementoProducto(
    producto: Producto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = "Código: ${producto.codigo}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Categoría: ${producto.categoria}", style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Stock: ${producto.stockActual}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (producto.stockActual <= producto.stockMinimo) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
                Text(text = "$${producto.precio}", style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}