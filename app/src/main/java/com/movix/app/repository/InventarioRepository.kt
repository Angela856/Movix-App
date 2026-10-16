package com.movix.app.repository

import com.movix.app.model.Movimiento
import com.movix.app.model.Producto
import com.movix.app.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InventarioRepository (private val productoDao: ProductoDao) {

    // 1. Devuelve la lista de usuarios desde los datos de prueba
    fun obtenerUsuarios(): List<Usuario> {
        return DatosDePrueba.usuarios
    }

    // 2. Listas reactivas inicializadas con los datos de prueba
    private val _productos = MutableStateFlow<List<Producto>>(DatosDePrueba.productosIniciales)
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    private val _movimientos = MutableStateFlow<List<Movimiento>>(DatosDePrueba.movimientosIniciales)
    val movimientos: StateFlow<List<Movimiento>> = _movimientos.asStateFlow()

    // 3. Métodos de consulta
    fun obtenerProductos(): List<Producto> {
        return _productos.value
    }

    fun obtenerMovimientos(): List<Movimiento> {
        return _movimientos.value
    }

    // Función que faltaba y solucionará el error en InventarioViewModel
    fun obtenerProductoPorCodigo(codigo: String): Producto? {
        return _productos.value.find { it.codigo.equals(codigo, ignoreCase = true) }
    }

    // Métodos para agregar o modificar datos
    fun agregarProducto(nuevoProducto: Producto) {
        _productos.value = _productos.value + nuevoProducto
    }

    fun registrarMovimiento(nuevoMovimiento: Movimiento) {
        _movimientos.value = listOf(nuevoMovimiento) + _movimientos.value
    }
}