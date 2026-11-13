package com.example.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

//preferencias "sesion" (uno solo para toda la app)
private val Context.dataStore by preferencesDataStore(name = "sesion")

// Guarda quién tiene la sesión iniciada, para recordarlo al cerrar la app
class SesionDataStore(private val context: Context) {

    private val claveUsuarioId = intPreferencesKey("usuario_id")

    // Flow: entrega el id guardado (o null) y avisa cada vez que cambia
    val usuarioId: Flow<Int?> = context.dataStore.data.map { prefs -> prefs[claveUsuarioId] }

    suspend fun leerUsuarioId(): Int? = usuarioId.first()

    suspend fun guardar(id: Int) {
        context.dataStore.edit { prefs -> prefs[claveUsuarioId] = id }
    }

    suspend fun borrar() {
        context.dataStore.edit { prefs -> prefs.remove(claveUsuarioId) }
    }
}