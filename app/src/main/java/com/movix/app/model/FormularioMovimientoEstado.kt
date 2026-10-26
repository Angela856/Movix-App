package com.movix.app.model

data class FormularioMovimientoEstado(
    val productoCodigo: String = "",
    val tipo: Movimiento.TipoMovimiento = Movimiento.TipoMovimiento.ENTRADA,
    val cantidadText: String = "1",
    val motivo: String = "",
    val errores: Errores = Errores()
) {
    data class Errores(
        val productoError: String? = null,
        val cantidadError: String? = null,
        val motivoError: String? = null
    ) {
        val tieneErrores: Boolean
            get() = productoError != null || cantidadError != null || motivoError != null
    }
}