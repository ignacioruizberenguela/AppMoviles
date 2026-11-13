package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.myapplication.model.Usuario

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM Usuario")
    suspend fun todos(): List<Usuario>

    @Query("SELECT COUNT(*) FROM Usuario")
    suspend fun contar(): Int

    @Upsert   // inserta si no existe, actualiza si ya existe
    suspend fun guardar(usuario: Usuario)

    @Upsert
    suspend fun guardarTodos(usuarios: List<Usuario>)

    @Query("DELETE FROM Usuario WHERE id = :id")
    suspend fun eliminar(id: Int)
}