package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.myapplication.model.Reserva

@Dao
interface ReservaDao {

    @Query("SELECT * FROM Reserva ORDER BY fecha")
    suspend fun todas(): List<Reserva>

    @Upsert
    suspend fun guardar(reserva: Reserva)

    @Upsert
    suspend fun guardarTodas(reservas: List<Reserva>)

    @Query("DELETE FROM Reserva WHERE id = :id")
    suspend fun eliminar(id: Int)
}