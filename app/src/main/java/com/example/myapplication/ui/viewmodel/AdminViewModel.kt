package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.ZarorRepository
import com.example.myapplication.model.*

class AdminViewModel : ViewModel() {

    private val repo = ZarorRepository

    // Listas (vienen del repositorio compartido)
    val usuarios get() = repo.usuarios
    val reservas get() = repo.reservas
    val movimientos get() = repo.movimientos

    // Resumen calculado con datos reales (ya no inventados)
    val totalVecinos: Int
        get() = usuarios.count { it.rol == Rol.VECINO }

    val solicitudesPendientes: Int
        get() = reservas.count { it.estado == EstadoReserva.PENDIENTE }

    val saldo: Int
        get() = movimientos.sumOf {
            if (it.tipo == TipoMovimiento.INGRESO) it.monto else -it.monto
        }

    // ---------- CRUD ----------
    fun guardarUsuario(usuario: Usuario) = repo.guardarUsuario(usuario)
    fun eliminarUsuario(usuario: Usuario) = repo.eliminarUsuario(usuario.id)

    fun cambiarEstadoReserva(reserva: Reserva, estado: EstadoReserva) =
        repo.guardarReserva(reserva.copy(estado = estado))
    fun eliminarReserva(reserva: Reserva) = repo.eliminarReserva(reserva.id)

    fun guardarMovimiento(movimiento: Movimiento) = repo.guardarMovimiento(movimiento)
    fun eliminarMovimiento(movimiento: Movimiento) = repo.eliminarMovimiento(movimiento.id)
}