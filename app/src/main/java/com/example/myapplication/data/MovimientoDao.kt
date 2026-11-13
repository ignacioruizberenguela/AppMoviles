package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.myapplication.model.Movimiento

@Dao
interface MovimientoDao {

    @Query("SELECT * FROM Movimiento ORDER BY id DESC")   // los más nuevos primero
    suspend fun todos(): List<Movimiento>

    @Upsert
    suspend fun guardar(movimiento: Movimiento)

    @Upsert
    suspend fun guardarTodos(movimientos: List<Movimiento>)

    @Query("DELETE FROM Movimiento WHERE id = :id")
    suspend fun eliminar(id: Int)
}