package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movimiento")
data class Movimiento(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tipo: TipoMovimiento,
    val categoria: Categoria,
    val monto: Int,
    val fecha: String,
    val descripcion: String,
    val reservaId: Int? = null,
    val comprobanteUri: String? = null
)