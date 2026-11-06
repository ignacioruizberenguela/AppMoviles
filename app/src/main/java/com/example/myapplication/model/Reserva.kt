package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Reserva")
data class Reserva(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val vecinoId: Int,
    val vecinoNombre: String,
    val fecha: String,
    val horario: Horario,
    val motivo: String,
    val estado: EstadoReserva = EstadoReserva.PENDIENTE, // valor default al crear rserva
    val comprobanteUri: String? = null
)