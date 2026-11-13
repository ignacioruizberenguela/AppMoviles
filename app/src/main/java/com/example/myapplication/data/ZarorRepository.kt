package com.example.myapplication.data

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import com.example.myapplication.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Fuente de datos única de la app.
// Las listas son lo que ve la UI; Room guarda los datos en el celular.
// Después: también sincronizará con la API de Spring Boot.
object ZarorRepository {

    // Lo que observan las pantallas (empiezan vacías y se llenan desde Room)
    val usuarios = mutableStateListOf<Usuario>()
    val reservas = mutableStateListOf<Reserva>()
    val movimientos = mutableStateListOf<Movimiento>()

    private lateinit var db: AppDatabase
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)  // trabajo en segundo plano

    // ---------- Datos de prueba (solo la primera vez que se abre la app) ----------
    private val usuariosIniciales = listOf(
        Usuario(1, "Admin Zaror", "11.111.111-1", "admin@zaror.cl", Rol.ADMIN, "1234"),
        Usuario(2, "María González", "12.222.222-2", "maria@correo.cl", Rol.VECINO, "1234"),
        Usuario(3, "Carlos Soto", "13.333.333-3", "carlos@correo.cl", Rol.VECINO, "1234"),
        Usuario(4, "Ana Rojas", "14.444.444-4", "tesoreria@zaror.cl", Rol.TESORERIA, "1234"),
        Usuario(5, "Pedro Muñoz", "15.555.555-5", "secretaria@zaror.cl", Rol.SECRETARIA, "1234"),
        Usuario(6, "Rosa Díaz", "16.666.666-6", "directiva@zaror.cl", Rol.DIRECTIVA, "1234")
    )

    private val reservasIniciales = listOf(
        Reserva(1, 2, "María González", "2026-11-15", Horario.TARDE, "Cumpleaños familiar"),
        Reserva(2, 3, "Carlos Soto", "2026-11-22", Horario.MANANA, "Reunión de comité")
    )

    private val movimientosIniciales = listOf(
        Movimiento(1, TipoMovimiento.INGRESO, Categoria.ARRIENDO, 25000, "2026-11-02", "Arriendo sede - cumpleaños"),
        Movimiento(2, TipoMovimiento.EGRESO, Categoria.LUZ, 18500, "2026-11-05", "Cuenta de luz octubre")
    )

    // ---------- Arranque: se llama una vez desde MainActivity ----------
    fun iniciar(context: Context) {
        if (::db.isInitialized) return   // ya estaba iniciado
        db = AppDatabase.getDatabase(context)

        scope.launch {
            // Primera vez: la base está vacía → cargamos los datos de prueba
            if (db.usuarioDao().contar() == 0) {
                db.usuarioDao().guardarTodos(usuariosIniciales)
                db.reservaDao().guardarTodas(reservasIniciales)
                db.movimientoDao().guardarTodos(movimientosIniciales)
            }

            // se lee la data desde Room...
            val u = db.usuarioDao().todos()
            val r = db.reservaDao().todas()
            val m = db.movimientoDao().todos()

            // ...y llenamos las listas en el hilo principal (el de la UI)
            withContext(Dispatchers.Main) {
                usuarios.clear(); usuarios.addAll(u)
                reservas.clear(); reservas.addAll(r)
                movimientos.clear(); movimientos.addAll(m)
            }
        }
    }

    // ---------- USUARIOS ----------
    // id = 0 → crear; id distinto de 0 → editar
    fun guardarUsuario(usuario: Usuario) {
        val final = if (usuario.id == 0) usuario.copy(id = siguienteId(usuarios.map { it.id })) else usuario
        val i = usuarios.indexOfFirst { it.id == final.id }
        if (i == -1) usuarios.add(final) else usuarios[i] = final
        enSegundoPlano { db.usuarioDao().guardar(final) }
    }

    fun eliminarUsuario(id: Int) {
        usuarios.removeAll { it.id == id }
        enSegundoPlano { db.usuarioDao().eliminar(id) }
    }

    // ---------- RESERVAS ----------
    fun guardarReserva(reserva: Reserva) {
        val final = if (reserva.id == 0) reserva.copy(id = siguienteId(reservas.map { it.id })) else reserva
        val i = reservas.indexOfFirst { it.id == final.id }
        if (i == -1) reservas.add(final) else reservas[i] = final
        enSegundoPlano { db.reservaDao().guardar(final) }
    }

    fun eliminarReserva(id: Int) {
        reservas.removeAll { it.id == id }
        enSegundoPlano { db.reservaDao().eliminar(id) }
    }

    // ---------- MOVIMIENTOS ----------
    fun guardarMovimiento(movimiento: Movimiento) {
        val final = if (movimiento.id == 0) {
            movimiento.copy(
                id = siguienteId(movimientos.map { it.id }),
                fecha = movimiento.fecha.ifBlank { fechaHoy() }
            )
        } else movimiento

        val i = movimientos.indexOfFirst { it.id == final.id }
        if (i == -1) movimientos.add(0, final) else movimientos[i] = final   // nuevos arriba
        enSegundoPlano { db.movimientoDao().guardar(final) }
    }

    fun eliminarMovimiento(id: Int) {
        movimientos.removeAll { it.id == id }
        enSegundoPlano { db.movimientoDao().eliminar(id) }
    }

    // ---------- Utilidades ----------
    // Ejecuta una operación de Room sin congelar la pantalla
    private fun enSegundoPlano(accion: suspend () -> Unit) {
        if (::db.isInitialized) scope.launch { accion() }
    }

    private fun siguienteId(ids: List<Int>): Int = (ids.maxOrNull() ?: 0) + 1

    fun fechaHoy(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
}