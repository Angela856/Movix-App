package com.movix.app.model

data class FormularioProductoEstado(
    val codigo: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val categoria: String = "Serigrafía",
    val tipo: String = "Polera Manga Corta",
    val precioText: String = "",
    val stockMinimoText: String = "5",
    val detalle: String = "",

    ) {
    data class Errores(
        val codigoError: String? = null,
        val nombreError: String? = null,
        val precioError: String? = null,
        val stockMinimoError: String? = null
    ) {
        val tieneErrores: Boolean
            get() = codigoError != null || nombreError != null || precioError != null || stockMinimoError != null
    }
}