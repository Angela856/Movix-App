package com.movix.app.model

data class FormularioLoginEstado(
    val correo: String = "",
    val contrasena: String = "",
    val usuarioSeleccionado: Usuario? = null,
    val error: String? = null
)