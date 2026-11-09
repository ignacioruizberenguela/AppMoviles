package com.example.myapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.ZarorRepository
import com.example.myapplication.model.Rol
import com.example.myapplication.model.Usuario

class AuthViewModel : ViewModel() {

    private val repo = ZarorRepository

    // Usuario con sesión iniciada (null = nadie)
    var usuarioActual by mutableStateOf<Usuario?>(null)
        private set

    // Devuelve null si salió bien, o el mensaje de error
    fun login(email: String, password: String): String? {
        if (email.isBlank() || password.isBlank()) return "Ingresa tu mail y contraseña"

        val usuario = repo.usuarios.find {
            it.email.equals(email.trim(), ignoreCase = true) && it.password == password
        } ?: return "Mail o contraseña incorrectos"

        usuarioActual = usuario
        return null
    }

    fun registrar(nombre: String, rut: String, email: String, password: String): String? {
        if (nombre.isBlank() || rut.isBlank() || email.isBlank() || password.isBlank()) {
            return "Completa todos los campos"
        }
        if (!rutValido(rut)) return "El RUT no es válido"
        if (!email.contains("@")) return "El mail no es válido"
        if (password.length < 4) return "La contraseña debe tener al menos 4 caracteres"
        if (repo.usuarios.any { it.email.equals(email.trim(), ignoreCase = true) }) {
            return "Ya existe una cuenta con ese mail"
        }
        if (repo.usuarios.any { limpiarRut(it.rut) == limpiarRut(rut) }) {
            return "Ya existe una cuenta con ese RUT"
        }

        // Todo el que se registra entra como VECINO; el Admin cambia roles después
        repo.guardarUsuario(
            Usuario(
                nombre = nombre.trim(),
                rut = rut.trim(),
                email = email.trim(),
                rol = Rol.VECINO,
                password = password
            )
        )
        usuarioActual = repo.usuarios.find { it.email.equals(email.trim(), ignoreCase = true) }
        return null
    }

    // Modo demo: entra con el primer usuario que tenga ese rol
    fun entrarComoDemo(rol: Rol) {
        usuarioActual = repo.usuarios.firstOrNull { it.rol == rol }
    }

    fun cerrarSesion() {
        usuarioActual = null
    }
}

// ---------- Validación de RUT chileno (módulo 11) ----------
private fun limpiarRut(rut: String): String =
    rut.replace(".", "").replace("-", "").uppercase()

fun rutValido(rut: String): Boolean {
    val limpio = limpiarRut(rut)
    if (limpio.length < 2) return false

    val cuerpo = limpio.dropLast(1)   // los números
    val dv = limpio.last()            // dígito verificador
    if (!cuerpo.all { it.isDigit() }) return false

    var suma = 0
    var multiplicador = 2
    for (digito in cuerpo.reversed()) {
        suma += digito.digitToInt() * multiplicador
        multiplicador = if (multiplicador == 7) 2 else multiplicador + 1
    }

    val esperado = when (val resto = 11 - (suma % 11)) {
        11 -> '0'
        10 -> 'K'
        else -> resto.digitToChar()
    }
    return dv == esperado
}