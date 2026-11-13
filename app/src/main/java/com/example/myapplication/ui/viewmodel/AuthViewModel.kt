package com.example.myapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.ZarorRepository
import com.example.myapplication.model.Rol
import com.example.myapplication.model.Usuario
import com.example.myapplication.util.limpiarRut
import com.example.myapplication.util.validarEmail
import com.example.myapplication.util.validarNombre
import com.example.myapplication.util.validarPassword
import com.example.myapplication.util.validarRut
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel : ViewModel() {

    private val repo = ZarorRepository

    // Usuario con sesión iniciada (null = nadie)
    var usuarioActual by mutableStateOf<Usuario?>(null)
        private set

    // Al crear el ViewModel, recupera la sesión guardada en DataStore (si hay)
    init {
        usuarioActual = repo.usuarioGuardado
    }

    // =====================================================
    // LOGIN
    // =====================================================
    // _loginState: privado, solo el ViewModel lo modifica
    // loginState: público y de solo lectura, la pantalla lo observa
    private val _loginState = MutableStateFlow(LoginUiState())
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    fun onLoginEmailChange(valor: String) {
        _loginState.update {
            it.copy(email = valor, errorEmail = validarEmail(valor), errorGeneral = null)
        }
    }

    fun onLoginPasswordChange(valor: String) {
        _loginState.update {
            it.copy(
                password = valor,
                errorPassword = if (valor.isBlank()) "Ingresa tu contraseña" else null,
                errorGeneral = null
            )
        }
    }

    // Devuelve true si inició sesión
    fun iniciarSesion(): Boolean {
        val estado = _loginState.value
        val errorEmail = validarEmail(estado.email)
        val errorPassword = if (estado.password.isBlank()) "Ingresa tu contraseña" else null

        // 1) Errores de formato → se muestran debajo de cada campo
        if (errorEmail != null || errorPassword != null) {
            _loginState.update { it.copy(errorEmail = errorEmail, errorPassword = errorPassword) }
            return false
        }

        // 2) Buscar al usuario
        val usuario = repo.usuarios.find {
            it.email.equals(estado.email.trim(), ignoreCase = true) && it.password == estado.password
        }
        if (usuario == null) {
            _loginState.update { it.copy(errorGeneral = "Mail o contraseña incorrectos") }
            return false
        }

        usuarioActual = usuario

        repo.guardarSesion(usuario)

        _loginState.value = LoginUiState()   // limpia el formulario
        return true
    }

    // =====================================================
    // REGISTRO
    // =====================================================
    private val _registroState = MutableStateFlow(RegistroUiState())
    val registroState: StateFlow<RegistroUiState> = _registroState.asStateFlow()

    fun onNombreChange(valor: String) {
        _registroState.update { it.copy(nombre = valor, errorNombre = validarNombre(valor), errorGeneral = null) }
    }

    fun onRutChange(valor: String) {
        _registroState.update { it.copy(rut = valor, errorRut = validarRut(valor), errorGeneral = null) }
    }

    fun onRegistroEmailChange(valor: String) {
        _registroState.update { it.copy(email = valor, errorEmail = validarEmail(valor), errorGeneral = null) }
    }

    fun onRegistroPasswordChange(valor: String) {
        _registroState.update { it.copy(password = valor, errorPassword = validarPassword(valor), errorGeneral = null) }
    }

    // Devuelve true si se registró
    fun registrar(): Boolean {
        val estado = _registroState.value

        // 1) Validar todos los campos (también los que nunca se tocaron)
        val validado = estado.copy(
            errorNombre = validarNombre(estado.nombre),
            errorRut = validarRut(estado.rut),
            errorEmail = validarEmail(estado.email),
            errorPassword = validarPassword(estado.password)
        )
        if (!validado.sinErrores) {
            _registroState.value = validado
            return false
        }

        // 2) Que no exista otra cuenta con el mismo mail o RUT
        if (repo.usuarios.any { it.email.equals(estado.email.trim(), ignoreCase = true) }) {
            _registroState.update { it.copy(errorEmail = "Ya existe una cuenta con ese mail") }
            return false
        }
        if (repo.usuarios.any { limpiarRut(it.rut) == limpiarRut(estado.rut) }) {
            _registroState.update { it.copy(errorRut = "Ya existe una cuenta con ese RUT") }
            return false
        }

        // 3) Crear la cuenta: el que se registra entra como VECINO
        repo.guardarUsuario(
            Usuario(
                nombre = estado.nombre.trim(),
                rut = estado.rut.trim(),
                email = estado.email.trim(),
                rol = Rol.VECINO,
                password = estado.password
            )
        )
        usuarioActual = repo.usuarios.find { it.email.equals(estado.email.trim(), ignoreCase = true) }
        usuarioActual?.let { repo.guardarSesion(it) }
        _registroState.value = RegistroUiState()
        return true
    }

    // =====================================================
    // SESIÓN
    // =====================================================
    // Modo demo: entra con el primer usuario que tenga ese rol
    fun entrarComoDemo(rol: Rol) {
        usuarioActual = repo.usuarios.firstOrNull { it.rol == rol }
        usuarioActual?.let { repo.guardarSesion(it) }
    }

    fun cerrarSesion() {
        repo.borrarSesion()
        usuarioActual = null
        _loginState.value = LoginUiState()
        _registroState.value = RegistroUiState()
    }
}