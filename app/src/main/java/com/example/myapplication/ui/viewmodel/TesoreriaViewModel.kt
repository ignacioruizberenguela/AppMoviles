package com.example.myapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.ZarorRepository
import com.example.myapplication.model.*
import java.util.Locale

class TesoreriaViewModel : ViewModel() {

    private val repo = ZarorRepository

    // ---------- Filtros (null = sin filtro) ----------
    var filtroTipo by mutableStateOf<TipoMovimiento?>(null)
        private set

    var filtroCategoria by mutableStateOf<Categoria?>(null)
        private set

    fun cambiarFiltroTipo(tipo: TipoMovimiento?) {
        filtroTipo = tipo
        filtroCategoria = null   // al cambiar el tipo, se limpia la categoría
    }

    fun cambiarFiltroCategoria(categoria: Categoria?) {
        filtroCategoria = categoria
    }

    // ---------- Listas ----------
    val movimientos get() = repo.movimientos

    val movimientosFiltrados: List<Movimiento>
        get() = movimientos.filter { mov ->
            (filtroTipo == null || mov.tipo == filtroTipo) &&
                    (filtroCategoria == null || mov.categoria == filtroCategoria)
        }

    // Reservas aprobadas que todavía no se pagan (para enlazar un ingreso de arriendo)
    val reservasPorPagar: List<Reserva>
        get() = repo.reservas.filter { it.estado == EstadoReserva.APROBADA }

    // ---------- Totales (para Informes) ----------
    val totalIngresos: Int
        get() = movimientos.filter { it.tipo == TipoMovimiento.INGRESO }.sumOf { it.monto }

    val totalEgresos: Int
        get() = movimientos.filter { it.tipo == TipoMovimiento.EGRESO }.sumOf { it.monto }

    val saldoTotal: Int
        get() = totalIngresos - totalEgresos

    // ---------- Reglas del negocio ----------
    // Qué categorías tienen sentido para cada tipo
    fun categoriasPara(tipo: TipoMovimiento): List<Categoria> = when (tipo) {
        TipoMovimiento.INGRESO -> listOf(Categoria.ARRIENDO, Categoria.CUOTA, Categoria.OTRO)
        TipoMovimiento.EGRESO -> listOf(
            Categoria.LUZ, Categoria.AGUA, Categoria.GAS, Categoria.MANTENCION, Categoria.OTRO
        )
    }

    fun guardarMovimiento(movimiento: Movimiento) {
        repo.guardarMovimiento(movimiento)

        // Si el ingreso es el pago de una reserva, la reserva queda PAGADA
        movimiento.reservaId?.let { id ->
            repo.reservas.find { it.id == id }?.let { reserva ->
                repo.guardarReserva(reserva.copy(estado = EstadoReserva.PAGADA))
            }
        }
    }

    fun eliminarMovimiento(movimiento: Movimiento) {
        repo.eliminarMovimiento(movimiento.id)
    }
}

// Formato chileno: 25000 → "$25.000"
fun formatoPesos(monto: Int): String =
    "$" + String.format(Locale("es", "CL"), "%,d", monto)