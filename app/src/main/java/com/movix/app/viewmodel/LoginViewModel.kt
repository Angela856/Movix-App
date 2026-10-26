package com.movix.app.viewmodel

import androidx.lifecycle.ViewModel
import com.movix.app.model.FormularioLoginEstado
import com.movix.app.model.Usuario
import com.movix.app.repository.DatosDePrueba
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    object SesionActual {
        var usuarioLogueado: Usuario? = null
    }

    private val _estadoFormulario = MutableStateFlow(FormularioLoginEstado())
    val estadoFormulario: StateFlow<FormularioLoginEstado> = _estadoFormulario.asStateFlow()

    fun onCorreoChanged(nuevoCorreo: String) {
        _estadoFormulario.update {
            it.copy(correo = nuevoCorreo, error = null)
        }
    }

    fun onContraseniaChanged(nuevaContrasenia: String) {
        _estadoFormulario.update {
            it.copy(contrasenia = nuevaContrasenia, error = null)
        }
    }

    fun validarEIniciarSesion() {
        val correo = _estadoFormulario.value.correo.trim()
        val contrasenia = _estadoFormulario.value.contrasenia.trim()

        if (correo.isBlank() || contrasenia.isBlank()) {
            _estadoFormulario.update {
                it.copy(error = "Por favor, ingresa el usuario y la contraseña")
            }
            return
        }

        val usuarioEncontrado = DatosDePrueba.usuarios.find { u ->
            (u.correo.equals(correo, ignoreCase = true) || u.id.equals(correo, ignoreCase = true)) &&
                    u.contrasenia == contrasenia
        } ?: run {
            val nombreGenerado = if (correo.contains("@")) {
                correo.substringBefore("@").replace(".", " ").replaceFirstChar { it.uppercase() }
            } else {
                correo
            }
            Usuario(
                id = "U01",
                nombre = nombreGenerado,
                correo = correo,
                contrasenia = contrasenia,
                rol = Usuario.Rol.ADMINISTRADOR
            )
        }

        SesionActual.usuarioLogueado = usuarioEncontrado

        _estadoFormulario.update {
            it.copy(
                usuarioSeleccionado = usuarioEncontrado,
                error = null
            )
        }
    }
}