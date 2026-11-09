package com.example.myapplication.ui.navigation

import com.example.myapplication.model.Rol
// menu lateral
data class ItemMenu(val titulo: String, val ruta: String)

// lista opciones segun rol
fun menuPorRol(rol: Rol?): List<ItemMenu> {
    return when (rol) {
        Rol.VECINO -> listOf(
            ItemMenu("Solicitar arriendo", Screen.VecinoFormulario.route),
            ItemMenu("Mis Reservas", Screen.MisReservas.route)

        )
        Rol.SECRETARIA -> listOf(
            ItemMenu("Solicitudes de arriendo", Screen.DirectivaSecretaria.route)
        )
        Rol.TESORERIA -> listOf(
            ItemMenu("Panel de Tesorería", Screen.Tesoreria.route),
            ItemMenu("Movimientos", Screen.TesoreriaMovimientos.route),
            ItemMenu("Informes", Screen.TesoreriaInformes.route)
        )
        Rol.DIRECTIVA -> listOf(
            ItemMenu("Panel de Directiva", Screen.Directiva.route),
            ItemMenu("Movimientos", Screen.TesoreriaMovimientos.route),
            ItemMenu("Informes", Screen.TesoreriaInformes.route)
        )
        Rol.ADMIN -> listOf(
            ItemMenu("Administración", Screen.Admin.route),
            ItemMenu("Solicitudes de arriendo", Screen.DirectivaSecretaria.route),
            ItemMenu("Panel de Tesorería", Screen.Tesoreria.route)
        )
        else -> emptyList()
    }
}