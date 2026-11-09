package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.ZarorRepository
import com.example.myapplication.model.*

class ReservaViewModel : ViewModel() {

    private val repo = ZarorRepository

    val reservas get() = repo.reservas

    val pendientes: List<Reserva>
        get() = reservas.filter { it.estado == EstadoReserva.PENDIENTE }

    fun reservasDe(usuario: Usuario): List<Reserva> =
        reservas.filter { it.vecinoId == usuario.id }

    // Estados que "ocupan" la sede (rechazadas y canceladas liberan el horario)
    private val estadosActivos = listOf(
        EstadoReserva.PENDIENTE, EstadoReserva.APROBADA, EstadoReserva.PAGADA
    )

    fun estaOcupado(fecha: String, horario: Horario): Boolean =
        reservas.any { it.fecha == fecha && it.horario == horario && it.estado in estadosActivos }

    // Devuelve null si salió bien, o el mensaje de error
    fun enviarSolicitud(usuario: Usuario, fecha: String, horario: Horario, motivo: String): String? {
        if (fecha.isBlank()) return "Elige una fecha"
        if (motivo.isBlank()) return "Escribe el motivo del arriendo"
        if (fecha < repo.fechaHoy()) return "No puedes reservar una fecha pasada"
        if (estaOcupado(fecha, horario)) return "Ese horario ya está tomado para esa fecha"

        repo.guardarReserva(
            Reserva(
                vecinoId = usuario.id,
                vecinoNombre = usuario.nombre,
                fecha = fecha,
                horario = horario,
                motivo = motivo.trim()
            )
        )
        return null
    }

    fun aprobar(reserva: Reserva) = cambiarEstado(reserva, EstadoReserva.APROBADA)
    fun rechazar(reserva: Reserva) = cambiarEstado(reserva, EstadoReserva.RECHAZADA)
    fun cancelar(reserva: Reserva) = cambiarEstado(reserva, EstadoReserva.CANCELADA)

    private fun cambiarEstado(reserva: Reserva, estado: EstadoReserva) {
        repo.guardarReserva(reserva.copy(estado = estado))
    }
}