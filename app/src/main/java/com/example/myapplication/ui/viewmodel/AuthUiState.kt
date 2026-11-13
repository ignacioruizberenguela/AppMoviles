package com.example.myapplication.ui.viewmodel

// Estado del formulario de login
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val errorGeneral: String? = null
)

// estado del formulario de registro
data class RegistroUiState(
    val nombre: String = "",
    val rut: String = "",
    val email: String = "",
    val password: String = "",
    val errorNombre: String? = null,
    val errorRut: String? = null,
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val errorGeneral: String? = null
) {
    val sinErrores: Boolean
        get() = errorNombre == null && errorRut == null && errorEmail == null && errorPassword == null
}