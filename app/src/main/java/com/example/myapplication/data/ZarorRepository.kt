package com.example.myapplication.data

import androidx.compose.runtime.mutableStateListOf
import com.example.myapplication.model.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Fuente de datos única de la app (en memoria por ahora).
// Después, por dentro, llamará a Room y a la API de Spring Boot.
object ZarorRepository {

    // ---------- Datos de prueba (ficticios) ----------
    val usuarios = mutableStateListOf(
        Usuario(1, "Admin Zaror", "11.111.111-1", "admin@zaror.cl", Rol.ADMIN, "1234"),
        Usuario(2, "María González", "12.222.222-2", "maria@correo.cl", Rol.VECINO, "1234"),
        Usuario(3, "Carlos Soto", "13.333.333-3", "carlos@correo.cl", Rol.VECINO, "1234"),
        Usuario(4, "Ana Rojas", "14.444.444-4", "tesoreria@zaror.cl", Rol.TESORERIA, "1234"),
        Usuario(5, "Pedro Muñoz", "15.555.555-5", "secretaria@zaror.cl", Rol.SECRETARIA, "1234"),
        Usuario(6, "Rosa Díaz", "16.666.666-6", "directiva@zaror.cl", Rol.DIRECTIVA, "1234")
    )

    val reservas = mutableStateListOf(
        Reserva(1, 2, "María González", "2026-11-15", Horario.TARDE, "Cumpleaños familiar"),
        Reserva(2, 3, "Carlos Soto", "2026-11-22", Horario.MANANA, "Reunión de comité")
    )

    val movimientos = mutableStateListOf(
        Movimiento(1, TipoMovimiento.INGRESO, Categoria.ARRIENDO, 25000, "2026-11-02", "Arriendo sede - cumpleaños"),
        Movimiento(2, TipoMovimiento.EGRESO, Categoria.LUZ, 18500, "2026-11-05", "Cuenta de luz octubre")
    )

    // ---------- USUARIOS ----------
    // id = 0 → crear; id distinto de 0 → editar
    fun guardarUsuario(usuario: Usuario) {
        if (usuario.id == 0) {
            usuarios.add(usuario.copy(id = siguienteId(usuarios.map { it.id })))
        } else {
            val i = usuarios.indexOfFirst { it.id == usuario.id }
            if (i != -1) usuarios[i] = usuario
        }
    }

    fun eliminarUsuario(id: Int) {
        usuarios.removeAll { it.id == id }
    }

    // ---------- RESERVAS ----------
    fun guardarReserva(reserva: Reserva) {
        if (reserva.id == 0) {
            reservas.add(reserva.copy(id = siguienteId(reservas.map { it.id })))
        } else {
            val i = reservas.indexOfFirst { it.id == reserva.id }
            if (i != -1) reservas[i] = reserva
        }
    }

    fun eliminarReserva(id: Int) {
        reservas.removeAll { it.id == id }
    }

    // ---------- MOVIMIENTOS ----------
    fun guardarMovimiento(movimiento: Movimiento) {
        if (movimiento.id == 0) {
            val nuevo = movimiento.copy(
                id = siguienteId(movimientos.map { it.id }),
                fecha = movimiento.fecha.ifBlank { fechaHoy() }
            )
            movimientos.add(0, nuevo) // los más nuevos primero
        } else {
            val i = movimientos.indexOfFirst { it.id == movimiento.id }
            if (i != -1) movimientos[i] = movimiento
        }
    }

    fun eliminarMovimiento(id: Int) {
        movimientos.removeAll { it.id == id }
    }

    // ---------- Utilidades ----------
    private fun siguienteId(ids: List<Int>): Int = (ids.maxOrNull() ?: 0) + 1

    fun fechaHoy(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
}