package com.movix.app.model

data class Usuario(
    val id: String,
    val nombre: String,
    val rol: Rol
) {
    enum class Rol {
        ADMINISTRADOR,
        VENDEDOR,
        ENCARGADO_INVENTARIO
    }
}