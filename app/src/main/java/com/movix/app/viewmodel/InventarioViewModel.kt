package com.movix.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movix.app.model.FormularioMovimientoEstado
import com.movix.app.model.Movimiento
import com.movix.app.model.Producto
import com.movix.app.repository.InventarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InventarioViewModel(
    private val repository: InventarioRepository
) : ViewModel() {

    private val _filtroBusqueda = MutableStateFlow("")
    val filtroBusqueda: StateFlow<String> = _filtroBusqueda.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow("Todas")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada.asStateFlow()

    private val _estadoFormularioMovimiento = MutableStateFlow(FormularioMovimientoEstado())
    val estadoFormularioMovimiento: StateFlow<FormularioMovimientoEstado> = _estadoFormularioMovimiento.asStateFlow()


    val productosFiltrados: StateFlow<List<Producto>> = combine(
        repository.productos,
        _filtroBusqueda,
        _categoriaSeleccionada
    ) { listaProductos, texto, categoria ->
        listaProductos.filter { producto ->
            val coincideTexto = producto.nombre.contains(texto, ignoreCase = true) ||
                    producto.codigo.contains(texto, ignoreCase = true)
            val coincideCategoria = categoria == "Todas" ||
                    producto.categoria.equals(categoria, ignoreCase = true)

            coincideTexto && coincideCategoria
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val productosEnAlerta: StateFlow<List<Producto>> = repository.productos
        .map { listaProductos ->
            listaProductos.filter { it.stockActual <= it.stockMinimo }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    val movimientos: StateFlow<List<Movimiento>> = repository.movimientos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun actualizarFiltroBusqueda(query: String) {
        _filtroBusqueda.value = query
    }

    fun seleccionarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }


    fun onProductoMovimientoChanged(codigo: String) {
        _estadoFormularioMovimiento.update {
            it.copy(
                productoCodigo = codigo,
                errores = it.errores.copy(productoError = null)
            )
        }
    }

    fun onTipoMovimientoChanged(nuevoTipo: Movimiento.TipoMovimiento) {
        _estadoFormularioMovimiento.update {
            it.copy(
                tipo = nuevoTipo,
                errores = it.errores.copy(motivoError = null)
            )
        }
    }

    fun onCantidadMovimientoChanged(cantidad: String) {
        _estadoFormularioMovimiento.update {
            it.copy(
                cantidadText = cantidad,
                errores = it.errores.copy(cantidadError = null)
            )
        }
    }

    fun onMotivoMovimientoChanged(motivo: String) {
        _estadoFormularioMovimiento.update {
            it.copy(
                motivo = motivo,
                errores = it.errores.copy(motivoError = null)
            )
        }
    }

    fun procesarFormularioMovimiento(usuarioActual: String, onExito: () -> Unit) {
        val estado = _estadoFormularioMovimiento.value
        val cantidadInt = estado.cantidadText.toIntOrNull()

        var errProducto: String? = null
        var errCantidad: String? = null
        var errMotivo: String? = null

        if (estado.productoCodigo.isBlank()) {
            errProducto = "Debe seleccionar un producto."
        }

        if (cantidadInt == null || cantidadInt <= 0) {
            errCantidad = "La cantidad debe ser un número entero mayor a 0."
        }

        if (estado.tipo == Movimiento.TipoMovimiento.AJUSTE && estado.motivo.isBlank()) {
            errMotivo = "Todo ajuste de inventario exige un motivo explícito."
        }

        val nuevosErrores = FormularioMovimientoEstado.Errores(
            productoError = errProducto,
            cantidadError = errCantidad,
            motivoError = errMotivo
        )

        if (nuevosErrores.tieneErrores) {
            _estadoFormularioMovimiento.update { it.copy(errores = nuevosErrores) }
            return
        }

        registrarMovimiento(
            codigoProducto = estado.productoCodigo,
            tipo = estado.tipo,
            cantidad = cantidadInt!!,
            motivo = estado.motivo,
            usuarioActual = usuarioActual
        ) { exito, mensajeError ->
            if (exito) {
                _estadoFormularioMovimiento.value = FormularioMovimientoEstado()
                onExito()
            } else {
                _estadoFormularioMovimiento.update {
                    it.copy(errores = it.errores.copy(cantidadError = mensajeError))
                }
            }
        }
    }


    fun registrarMovimiento(
        codigoProducto: String,
        tipo: Movimiento.TipoMovimiento,
        cantidad: Int,
        motivo: String,
        usuarioActual: String,
        onResultado: (exito: Boolean, mensaje: String?) -> Unit
    ) {
        if (cantidad <= 0) {
            onResultado(false, "La cantidad debe ser mayor que cero.")
            return
        }

        if (tipo == Movimiento.TipoMovimiento.AJUSTE && motivo.isBlank()) {
            onResultado(false, "Todo ajuste exige indicar un motivo.")
            return
        }

        viewModelScope.launch {
            val producto = repository.obtenerProductoPorCodigo(codigoProducto)
            if (producto == null) {
                onResultado(false, "El producto no existe.")
                return@launch
            }

            val esSalida = tipo == Movimiento.TipoMovimiento.SALIDA_VENTA ||
                    tipo == Movimiento.TipoMovimiento.SALIDA_CONSUMO

            if (esSalida && cantidad > producto.stockActual) {
                onResultado(false, "La cantidad a retirar supera el stock disponible.")
                return@launch
            }

            val nuevoStock = when (tipo) {
                Movimiento.TipoMovimiento.ENTRADA -> producto.stockActual + cantidad
                Movimiento.TipoMovimiento.SALIDA_VENTA,
                Movimiento.TipoMovimiento.SALIDA_CONSUMO -> producto.stockActual - cantidad
                Movimiento.TipoMovimiento.AJUSTE -> cantidad
            }

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val fechaActual = sdf.format(Date())

            val nuevoMovimiento = Movimiento(
                id = "M${System.currentTimeMillis()}",
                codigoProducto = codigoProducto,
                tipo = tipo,
                cantidad = cantidad,
                motivo = motivo,
                fecha = fechaActual,
                usuario = usuarioActual
            )

            repository.registrarMovimiento(nuevoMovimiento)
            repository.actualizarProducto(producto.copy(stockActual = nuevoStock))

            onResultado(true, null)
        }
    }

    fun agregarProducto(nuevoProducto: Producto, onExito: () -> Unit) {
        viewModelScope.launch {
            repository.agregarProducto(nuevoProducto)
            onExito()
        }
    }
}