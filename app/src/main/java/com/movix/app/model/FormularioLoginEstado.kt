package com.movix.app.model

data class FormularioLoginEstado(
    val correo: String = "",
    val contrasenia: String = "",
    val usuarioSeleccionado: Usuario? = null,
    val error: String? = null
)