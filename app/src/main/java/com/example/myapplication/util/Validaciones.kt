package com.example.myapplication.util

// cada funcion devuelve el mensaje de error correspondiente o null si el valor es valido

fun validarNombre(nombre: String): String? = when {
    nombre.isBlank() -> "Ingresa tu nombre"
    nombre.trim().length < 3 -> "El nombre es muy corto"
    else -> null
}

fun validarEmail(email: String): String? = when {
    email.isBlank() -> "Ingresa tu mail"
    !Regex("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$").matches(email.trim()) -> "Mail no válido"
    else -> null
}

fun validarPassword(password: String): String? = when {
    password.isBlank() -> "Ingresa una contraseña"
    password.length < 4 -> "Mínimo 4 caracteres"
    else -> null
}

fun validarRut(rut: String): String? = when {
    rut.isBlank() -> "Ingresa tu RUT"
    !rutValido(rut) -> "RUT inválido (ej: 12.345.678-5)"
    else -> null
}

// ---------- RUT chileno ----------
fun limpiarRut(rut: String): String =
    rut.replace(".", "").replace("-", "").trim().uppercase()

fun rutValido(rut: String): Boolean {
    val limpio = limpiarRut(rut)
    if (limpio.length < 2) return false

    val cuerpo = limpio.dropLast(1)
    val dv = limpio.last()
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