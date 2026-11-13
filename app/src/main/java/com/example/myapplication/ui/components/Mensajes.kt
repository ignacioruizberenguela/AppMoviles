package com.example.myapplication.ui.components

import androidx.compose.runtime.staticCompositionLocalOf

// Función global para mostrar mensajes (Snackbar) desde cualquier pantalla.
// ZarorApp la "provee"; las pantallas la usan con: val avisar = LocalMensajes.current
// Por defecto no hace nada (así los Previews no fallan).
val LocalMensajes = staticCompositionLocalOf<(String) -> Unit> { {} }