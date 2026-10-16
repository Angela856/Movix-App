package com.movix.app.navigation

sealed class Rutas(val ruta: String) {
    object Login : Rutas("login")
    object Catálogo : Rutas("catalogo")
    object Movimiento : Rutas("movimiento")
    object Alertas : Rutas("alertas")
    object FormularioProducto : Rutas("formulario_producto")
    object DetalleProducto : Rutas("detalle_producto")
}