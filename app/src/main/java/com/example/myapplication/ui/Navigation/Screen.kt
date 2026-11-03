package com.example.myapplication.ui.navigation


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object VecinoFormulario : Screen("vecino_formulario")
    object DirectivaSecretaria : Screen("directiva_secretaria")
    object Tesoreria : Screen("tesoreria")
    object TesoreriaIngresos : Screen("tesoreria_ingresos")
    object TesoreriaEgresos : Screen("tesoreria_egresos")
    object TesoreriaInformes : Screen("tesoreria_informes")
    object Directiva : Screen("directiva")
    object Admin : Screen("admin")
}

