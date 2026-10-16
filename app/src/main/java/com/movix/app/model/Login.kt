package com.movix.app.model

data class Login(
    val correo: String = "",
    val contrasena: String = "",
    val error: String? = null
)