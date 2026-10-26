package com.movix.app.model
data class Movimiento(
    val id: String,
    val codigoProducto: String,
    val tipo: TipoMovimiento,
    val cantidad: Int,
    val motivo: String = "",
    val fecha: String,
    val usuario: String
) {
    enum class TipoMovimiento {
        ENTRADA,
        SALIDA_VENTA,
        SALIDA_CONSUMO,
        AJUSTE
    }
}