package com.movix.app.viewmodel

import androidx.lifecycle.ViewModel
import com.movix.app.model.FormularioLoginEstado
import com.movix.app.model.Usuario
import com.movix.app.repository.InventarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {


    private val repository = InventarioRepository()


    val usuariosDisponibles: List<Usuario> = repository.obtenerUsuarios()


    private val _estadoFormulario = MutableStateFlow(FormularioLoginEstado())
    val estadoFormulario: StateFlow<FormularioLoginEstado> = _estadoFormulario.asStateFlow()


    private val _usuarioActivo = MutableStateFlow<Usuario?>(null)
    val usuarioActivo: StateFlow<Usuario?> = _usuarioActivo.asStateFlow()


    fun actualizarCorreo(correo: String) {
        _estadoFormulario.update {
            it.copy(correo = correo, error = null)
        }
    }

    fun actualizarContrasena(contrasena: String) {
        _estadoFormulario.update {
            it.copy(contrasena = contrasena, error = null)
        }
    }


    fun validarYIngresar(): Boolean {
        val estado = _estadoFormulario.value

        // Validación básica: verificar que no esté vacío el campo de texto
        if (estado.correo.isBlank()) {
            _estadoFormulario.update {
                it.copy(error = "Debe ingresar un nombre de usuario")
            }
            return false
        }


        val usuarioEncontrado = usuariosDisponibles.find {
            it.nombre.equals(estado.correo.trim(), ignoreCase = true)
        } ?: usuariosDisponibles.firstOrNull() // Opcional: si escribes cualquier cosa, toma el primero por defecto para facilitar pruebas

        if (usuarioEncontrado == null) {
            _estadoFormulario.update {
                it.copy(error = "El usuario ingresado no existe")
            }
            return false
        }


        _estadoFormulario.update {
            it.copy(usuarioSeleccionado = usuarioEncontrado, error = null)
        }
        _usuarioActivo.value = usuarioEncontrado
        return true
    }


    fun cerrarSesion() {
        _usuarioActivo.value = null
        _estadoFormulario.value = FormularioLoginEstado()
    }
}