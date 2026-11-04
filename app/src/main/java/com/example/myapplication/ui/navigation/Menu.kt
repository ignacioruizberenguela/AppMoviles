package com.example.myapplication.ui.navigation

// menu lateral
data class ItemMenu(val titulo: String, val ruta: String)

// lista opciones segun rol
fun menuPorRol(rol: String): List<ItemMenu> {
    return when (rol) {
        "Vecino" -> listOf(
            ItemMenu("Solicitar arriendo", Screen.VecinoFormulario.route)
        )
        "Secretaría" -> listOf(
            ItemMenu("Solicitudes de arriendo", Screen.DirectivaSecretaria.route)
        )
        "Tesorería" -> listOf(
            ItemMenu("Panel de Tesorería", Screen.Tesoreria.route),
            ItemMenu("Ingresos", Screen.TesoreriaIngresos.route),
            ItemMenu("Egresos", Screen.TesoreriaEgresos.route),
            ItemMenu("Informes", Screen.TesoreriaInformes.route)
        )
        "Directiva" -> listOf(
            ItemMenu("Panel de Directiva", Screen.Directiva.route),
            ItemMenu("Informes", Screen.TesoreriaInformes.route)
        )
        "Admin" -> listOf(
            ItemMenu("Administración", Screen.Admin.route),
            ItemMenu("Solicitudes de arriendo", Screen.DirectivaSecretaria.route),
            ItemMenu("Panel de Tesorería", Screen.Tesoreria.route)
        )
        else -> emptyList()
    }
}