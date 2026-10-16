package com.movix.app.viewmodel

import androidx.lifecycle.ViewModel
import com.movix.app.model.Movimiento
import com.movix.app.model.Producto
import com.movix.app.repository.InventarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InventarioViewModel : ViewModel() {

    // Instancia del repositorio
    private val repository = InventarioRepository()

    // --- ESTADOS EXPUESTOS CON STATEFLOW ---

    // 1. Lista completa de productos (obtenida del repositorio)
    private val _productos = MutableStateFlow(repository.obtenerProductos())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    // 2. Texto para la barra de búsqueda y filtros
    private val _filtroBusqueda = MutableStateFlow("")
    val filtroBusqueda: StateFlow<String> = _filtroBusqueda.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow("Todas")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada.asStateFlow()

    // --- FUNCIONES DE ACCIÓN Y NEGOCIO ---

    // Actualiza el texto de búsqueda para filtrar el catálogo
    fun actualizarFiltroBusqueda(query: String) {
        _filtroBusqueda.value = query
    }

    // Actualiza la categoría seleccionada
    fun seleccionarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }

    // Recarga o actualiza la lista de productos desde el repositorio
    fun refrescarProductos() {
        _productos.update { repository.obtenerProductos() }
    }

    // Registrar un movimiento (entrada, salida o ajuste) y actualizar el stock automáticamente
    fun registrarMovimiento(
        codigoProducto: String,
        tipo: Movimiento.TipoMovimiento,
        cantidad: Int,
        motivo: String,
        usuarioActual: String
    ): Boolean {
        // Regla de negocio RN-01: Una salida nunca puede dejar la cantidad disponible bajo cero
        val producto = repository.obtenerProductoPorCodigo(codigoProducto) ?: return false

        if ((tipo == Movimiento.TipoMovimiento.SALIDA_VENTA || tipo == Movimiento.TipoMovimiento.SALIDA_CONSUMO)
            && cantidad > producto.stockActual) {
            return false // No se puede sacar más de lo que hay disponible
        }

        // Crear el objeto movimiento
        val nuevoMovimiento = Movimiento(
            id = System.currentTimeMillis().toString(),
            codigoProducto = codigoProducto,
            tipo = tipo,
            cantidad = cantidad,
            motivo = motivo,
            fecha = java.time.LocalDate.now().toString(), // O formato de fecha correspondiente
            usuario = usuarioActual
        )

        // Registrar en el repositorio (esto actualiza el stock internamente en tu repo)
        repository.registrarMovimiento(nuevoMovimiento)

        // Refrescamos la lista en el ViewModel para que la UI se entere del cambio de stock
        refrescarProductos()
        return true
    }

    // Filtrar productos con stock bajo (Regla RN-06: stockActual <= stockMinimo)
    fun obtenerProductosConStockBajo(): List<Producto> {
        return repository.obtenerProductos().filter { it.stockActual <= it.stockMinimo }
    }
}